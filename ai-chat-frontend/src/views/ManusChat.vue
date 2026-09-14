<template>
  <div class="page-wrapper">
    <ChatRoom
      title="AI 超级智能体"
      :generateChatId="false"
      storageKey="manus_chat_messages"
      :onSend="handleSend"
    />
  </div>
</template>

<script setup lang="ts">
import ChatRoom from '../components/ChatRoom.vue';

const baseUrl = '/api/ai';

const handleSend = (
  message: string,
  _chatId: string | undefined,
  onChunk: (chunk: string, es?: EventSource) => void,
  onComplete: () => void,
  onError: (msg: string) => void
) => {
  try {
    const params = new URLSearchParams();
    params.append('message', message);

    const url = `${baseUrl}/manus/chat?${params.toString()}`;
    const es = new EventSource(url);
    let receivedAny = false;
    let settled = false;
    let lastMsgAt = Date.now();
    let finishTimer: number | null = null;

    const clearFinishTimer = () => {
      if (finishTimer !== null) {
        window.clearTimeout(finishTimer);
        finishTimer = null;
      }
    };

    const finish = () => {
      if (settled) return;
      settled = true;
      clearFinishTimer();
      es.close();
      onComplete();
    };

    es.onmessage = (event) => {
      const data = event.data || '';
      if (!data) return;
      receivedAny = true;
      lastMsgAt = Date.now();
      clearFinishTimer();
      onChunk(data, es);
    };

    es.onerror = () => {
      if (settled) return;

      if (!receivedAny) {
        settled = true;
        es.close();
        onError('SSE 连接出错');
        return;
      }

      clearFinishTimer();
      finishTimer = window.setTimeout(() => {
        const idleMs = Date.now() - lastMsgAt;
        if (idleMs >= 800 || es.readyState === EventSource.CLOSED) {
          finish();
        }
      }, 900);
    };
  } catch (e: any) {
    onError(e?.message || '发送失败');
  }
};
</script>
