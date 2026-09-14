<template>
  <div class="chat-room">
    <div class="chat-header">
      <div class="chat-title">{{ title }}</div>
      <div v-if="chatId" class="chat-sub-title">会话 ID：{{ chatId }}</div>
    </div>

    <div class="chat-messages" ref="messageContainer">
      <template v-for="(msg, index) in messages" :key="index">
        <div
          v-for="(chunk, cIndex) in splitContent(msg)"
          :key="cIndex"
          class="chat-message-row"
          :class="msg.role === 'user' ? 'is-user' : 'is-ai'"
        >
          <div class="avatar">
            <span v-if="msg.role === 'user'">我</span>
            <span v-else>AI</span>
          </div>
          <div class="bubble">
            <div class="bubble-text">{{ chunk }}</div>
            <div class="bubble-time">{{ msg.time }}</div>
          </div>
        </div>
      </template>
    </div>

    <div class="chat-input-area">
      <textarea
        v-model="input"
        class="chat-input"
        placeholder="请输入消息，回车发送（Shift+回车换行）"
        @keydown.enter.prevent="handleEnter"
      ></textarea>
      <div class="chat-input-actions">
        <button
          class="btn"
          :disabled="sending || !input.trim()"
          @click="handleSend"
        >
          {{ sending ? 'AI 正在回复...' : '发送' }}
        </button>
        <button class="btn btn-secondary" :disabled="sending" @click="handleClear">
          清空会话
        </button>
        <button v-if="showNewSessionBtn" class="btn btn-secondary" :disabled="sending" @click="handleNewSession">
          新增会话
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, onBeforeUnmount, computed } from 'vue';

interface ChatMessage {
  role: 'user' | 'ai';
  content: string;
  time: string;
}

const props = defineProps<{
  title: string;
  generateChatId?: boolean;
  storageKey?: string;
  chatIdStorageKey?: string;
  onSend: (
    message: string,
    chatId: string | undefined,
    onChunk: (chunk: string, es?: EventSource) => void,
    onComplete: () => void,
    onError: (msg: string) => void
  ) => void;
}>();

const messages = ref<ChatMessage[]>([]);
const input = ref('');
const sending = ref(false);
const chatId = ref('');
const messageContainer = ref<HTMLElement | null>(null);

let generatingStreamMessage: ChatMessage | null = null;
let eventSource: EventSource | null = null;
let typingQueue: string[] = [];
let typingTimer: number | null = null;
const TYPE_INTERVAL_MS = 30;

const showNewSessionBtn = computed(() => props.generateChatId);

const nowText = () => {
  const d = new Date();
  const pad = (n: number) => (n < 10 ? '0' + n : '' + n);
  return `${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
};

const scrollToBottom = () => {
  if (!messageContainer.value) return;
  requestAnimationFrame(() => {
    if (!messageContainer.value) return;
    messageContainer.value.scrollTop = messageContainer.value.scrollHeight;
  });
};

watch(
  messages,
  () => {
    scrollToBottom();
  },
  { deep: true }
);

watch(
  chatId,
  () => {
    saveChatId();
  }
);

const handleEnter = (e: KeyboardEvent) => {
  if (e.shiftKey) {
    input.value += '\n';
  } else {
    handleSend();
  }
};

const splitContent = (msg: ChatMessage): string[] => {
  if (msg.role !== 'ai') {
    return [msg.content];
  }
  const text = msg.content;
  const pattern = /(Step\s+\d+:[\s\S]*?)(?=Step\s+\d+:|$)/g;
  const result: string[] = [];
  let match: RegExpExecArray | null;
  while ((match = pattern.exec(text)) !== null) {
    const chunk = match[1].trim();
    if (chunk) {
      result.push(chunk);
    }
  }
  if (result.length === 0) {
    return [text];
  }
  return result;
};

const startTypingTimer = () => {
  if (typingTimer !== null) return;
  
  typingTimer = window.setInterval(() => {
    if (typingQueue.length === 0) {
      if (typingTimer !== null) {
        window.clearInterval(typingTimer);
        typingTimer = null;
      }
      return;
    }
    
    const char = typingQueue.shift();
    if (char && generatingStreamMessage) {
      generatingStreamMessage.content += char;
    }
  }, TYPE_INTERVAL_MS);
};

const stopTypingTimer = () => {
  if (typingTimer !== null) {
    window.clearInterval(typingTimer);
    typingTimer = null;
  }
};

const appendStreamChunk = (chunk: string) => {
  if (!chunk) return;
  
  if (!generatingStreamMessage) {
    generatingStreamMessage = {
      role: 'ai',
      content: '',
      time: nowText()
    };
    messages.value.push(generatingStreamMessage);
  }
  
  for (const char of chunk) {
    typingQueue.push(char);
  }
  
  startTypingTimer();
};

const endStreamMessage = () => {
  stopTypingTimer();
  if (generatingStreamMessage && typingQueue.length > 0) {
    generatingStreamMessage.content += typingQueue.join('');
    typingQueue = [];
  }
  generatingStreamMessage = null;
  // 消息完全生成后才保存
  saveMessages();
};

const closeEventSource = () => {
  if (eventSource) {
    eventSource.close();
    eventSource = null;
  }
};

const saveMessages = () => {
  if (props.storageKey) {
    localStorage.setItem(props.storageKey, JSON.stringify(messages.value));
  }
};

const loadMessages = () => {
  if (props.storageKey) {
    const saved = localStorage.getItem(props.storageKey);
    if (saved) {
      try {
        messages.value = JSON.parse(saved);
      } catch (e) {
        console.error('Failed to load messages:', e);
      }
    }
  }
};

const saveChatId = () => {
  if (props.chatIdStorageKey && chatId.value) {
    localStorage.setItem(props.chatIdStorageKey, chatId.value);
  }
};

const loadChatId = () => {
  if (props.chatIdStorageKey) {
    const saved = localStorage.getItem(props.chatIdStorageKey);
    if (saved) {
      chatId.value = saved;
    }
  }
};

const generateNewChatId = () => {
  return 'chat_' +
    Date.now().toString(36) +
    '_' +
    Math.random().toString(16).slice(2, 8);
};

const handleSend = () => {
  const text = input.value.trim();
  if (!text || sending.value) return;

  messages.value.push({
    role: 'user',
    content: text,
    time: nowText()
  });
  // 用户消息立即保存
  saveMessages();

  input.value = '';
  sending.value = true;

  props.onSend(
    text,
    chatId.value || undefined,
    (chunk, es) => {
      if (es && !eventSource) {
        eventSource = es;
      }
      appendStreamChunk(chunk);
    },
    () => {
      sending.value = false;
      endStreamMessage();
      closeEventSource();
    },
    (errorMessage) => {
      endStreamMessage();
      closeEventSource();
      messages.value.push({
        role: 'ai',
        content: `出错了：${errorMessage}`,
        time: nowText()
      });
      saveMessages();
      sending.value = false;
    }
  );
};

const handleClear = () => {
  messages.value = [];
  saveMessages();
};

const handleNewSession = () => {
  messages.value = [];
  saveMessages();
  chatId.value = generateNewChatId();
};

onMounted(() => {
  loadMessages();
  if (props.generateChatId) {
    loadChatId();
    if (!chatId.value) {
      chatId.value = generateNewChatId();
    }
  }
});

onBeforeUnmount(() => {
  closeEventSource();
  stopTypingTimer();
  // 页面卸载时保存当前消息
  saveMessages();
});
</script>
