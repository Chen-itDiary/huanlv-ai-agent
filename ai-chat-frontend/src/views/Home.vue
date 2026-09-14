<template>
  <div class="home-page">
    <section class="home-hero">
      <div class="home-hero-text">
        <h2>一键切换你的专属 AI 角色</h2>
        <p>
          为旅游、工作与创意打造的多场景智能体中控台。
          在同一套界面下快速切换不同人格、不同能力的 AI。
        </p>
        <div class="home-meta">
          <span>流式对话</span>
          <span>工具调用</span>
          <span>桌面 / 移动自适应</span>
        </div>
      </div>
      <div class="home-hero-panel">
        <div class="home-hero-card">
          <div class="hero-card-header">
            <span class="dot dot-red"></span>
            <span class="dot dot-yellow"></span>
            <span class="dot dot-green"></span>
          </div>
          <div class="hero-card-body">
            <div class="hero-code-line">
              <span class="token-faded">// 当前智能体</span>
            </div>
            <div class="hero-code-line">
              <span class="token-key">agent</span>
              <span class="token-op">=</span>
              <span class="token-str">"AI 旅游大师"</span>
            </div>
            <div class="hero-code-line">
              <span class="token-key">mode</span>
              <span class="token-op">=</span>
              <span class="token-str">"对话 · 旅游规划"</span>
            </div>
            <div class="hero-code-line">
              <span class="token-key">switchTo</span>
              <span class="token-op">(</span>
              <span class="token-str">"AI 超级智能体"</span>
              <span class="token-op">)</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="home-grid">
      <div class="card" @click="$router.push('/love')">
        <div class="card-icon travel-icon">
          <svg viewBox="0 0 48 48" aria-hidden="true">
            <defs>
              <linearGradient id="travel-grad" x1="0" y1="0" x2="1" y2="1">
                <stop offset="0" stop-color="#f59e0b" />
                <stop offset="1" stop-color="#ec4899" />
              </linearGradient>
            </defs>
            <!-- 飞机 -->
            <path
              d="M24 8c-2 0-3.5 1-4 2.5l-8 16c-.5 1-.5 2 0 3l8 16c.5 1.5 2 2.5 4 2.5s3.5-1 4-2.5l8-16c.5-1 .5-2 0-3l-8-16c-.5-1.5-2-2.5-4-2.5Z"
              fill="url(#travel-grad)"
              opacity="0.8"
            />
            <!-- 飞机窗口 -->
            <circle cx="24" cy="16" r="2" fill="rgba(248,250,252,0.9)" />
            <circle cx="24" cy="24" r="2" fill="rgba(248,250,252,0.9)" />
            <circle cx="24" cy="32" r="2" fill="rgba(248,250,252,0.9)" />
            <!-- 地球 -->
            <circle cx="12" cy="38" r="6" fill="none" stroke="url(#travel-grad)" stroke-width="1.2" />
            <path d="M8 38h8M12 34v8" stroke="url(#travel-grad)" stroke-width="1" stroke-linecap="round" />
            <!-- 指南针 -->
            <circle cx="36" cy="38" r="5" fill="none" stroke="url(#travel-grad)" stroke-width="1.2" />
            <path d="M36 33v10M31 38h10" stroke="url(#travel-grad)" stroke-width="1" stroke-linecap="round" />
            <circle cx="36" cy="38" r="1.5" fill="url(#travel-grad)" />
          </svg>
        </div>
        <div class="card-content">
          <h3>AI 旅游大师</h3>
          <p>为旅行爱好者量身打造的智能旅游顾问，提供目的地推荐、行程规划与旅行建议。</p>
          <div class="card-tags">
            <span>目的地推荐</span>
            <span>行程规划</span>
            <span>旅行建议</span>
          </div>
        </div>
      </div>

      <div class="card" @click="$router.push('/manus')">
        <div class="card-icon manus-icon">
          <svg viewBox="0 0 48 48" aria-hidden="true">
            <defs>
              <linearGradient id="manus-grad" x1="0" y1="0" x2="1" y2="1">
                <stop offset="0" stop-color="#22c55e" />
                <stop offset="1" stop-color="#0ea5e9" />
              </linearGradient>
            </defs>
            <rect
              x="8"
              y="10"
              width="32"
              height="24"
              rx="6"
              fill="rgba(15,23,42,0.95)"
              stroke="url(#manus-grad)"
              stroke-width="1.6"
            />
            <path
              d="M16 22h4l2-4 4 8 2-4h4"
              fill="none"
              stroke="url(#manus-grad)"
              stroke-width="1.8"
              stroke-linecap="round"
              stroke-linejoin="round"
            />
            <circle cx="16" cy="16" r="1.4" fill="#22c55e" />
            <circle cx="32" cy="16" r="1.4" fill="#38bdf8" />
          </svg>
        </div>
        <div class="card-content">
          <h3>AI 超级智能体</h3>
          <p>多工具协作的通用大脑，用于搜索、分析、规划和长链路推理任务。</p>
          <div class="card-tags">
            <span>工具编排</span>
            <span>复杂任务分解</span>
            <span>可解释推理</span>
          </div>
        </div>
      </div>
    </section>

    <section class="home-bottom">
      <div class="home-features">
        <div class="feature-card">
          <h4>旅游大师</h4>
          <p>专注旅游规划与目的地推荐，适合行程安排、景点介绍与旅行建议。</p>
        </div>
        <div class="feature-card">
          <h4>超级智能体</h4>
          <p>多步推理与工具调用，适合搜索、总结与复杂任务拆解。</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
</script>
