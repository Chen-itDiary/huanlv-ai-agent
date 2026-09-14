<template>
  <div class="meteor-sky" aria-hidden="true">
    <span
      v-for="(m, i) in meteors"
      :key="i"
      class="meteor"
      :style="{
        left: m.left + '%',
        top: m.top + '%',
        animationDelay: m.delay + 's',
        animationDuration: m.duration + 's'
      }"
    />
  </div>
</template>

<script setup lang="ts">
const meteors = [
  { left: 8, top: 5, delay: 0, duration: 4.2 },
  { left: 18, top: 2, delay: 1.2, duration: 5.1 },
  { left: 35, top: 8, delay: 2.5, duration: 3.8 },
  { left: 52, top: 3, delay: 0.8, duration: 4.6 },
  { left: 68, top: 12, delay: 3.1, duration: 5.4 },
  { left: 82, top: 6, delay: 1.8, duration: 4 },
  { left: 92, top: 15, delay: 4.2, duration: 6 },
  { left: 12, top: 18, delay: 2.2, duration: 4.4 },
  { left: 45, top: 22, delay: 5, duration: 5.2 },
  { left: 75, top: 25, delay: 3.6, duration: 4.8 },
  { left: 28, top: 35, delay: 6.5, duration: 5.8 },
  { left: 58, top: 40, delay: 7, duration: 4.1 }
];
</script>

<style scoped>
.meteor-sky {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;
}

.meteor {
  position: absolute;
  width: 1px;
  height: 1px;
  background: transparent;
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.15);
  animation-name: meteor-fall;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
}

.meteor::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 120px;
  height: 2px;
  transform-origin: left center;
  transform: rotate(-35deg) translateX(0);
  background: linear-gradient(
    90deg,
    rgba(255, 255, 255, 0.95),
    rgba(147, 197, 253, 0.6),
    rgba(99, 102, 241, 0.15),
    transparent
  );
  border-radius: 2px;
  filter: blur(0.3px);
  box-shadow: 0 0 12px rgba(165, 180, 252, 0.55);
}

@keyframes meteor-fall {
  0% {
    opacity: 0;
    transform: translate3d(0, 0, 0) scale(0.6);
  }
  8% {
    opacity: 1;
  }
  70% {
    opacity: 1;
  }
  100% {
    opacity: 0;
    transform: translate3d(-280px, 320px, 0) scale(1);
  }
}
</style>
