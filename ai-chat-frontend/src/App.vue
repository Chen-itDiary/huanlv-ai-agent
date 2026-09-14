<template>
  <MeteorShower />
  <div class="app-root">
    <header class="app-header">
      <h1>AI 多应用中心</h1>
      <nav class="nav">
        <RouterLink to="/">主页</RouterLink>
        <RouterLink to="/love">AI 旅游大师</RouterLink>
        <RouterLink to="/manus">AI 超级智能体</RouterLink>
      </nav>
    </header>
    <main class="app-main">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
import MeteorShower from './components/MeteorShower.vue';
</script>
