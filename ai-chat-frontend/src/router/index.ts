import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import Home from '../views/Home.vue';
import LoveChat from '../views/LoveChat.vue';
import ManusChat from '../views/ManusChat.vue';

const routes: Array<RouteRecordRaw> = [
  { path: '/', name: 'Home', component: Home },
  { path: '/love', name: 'LoveChat', component: LoveChat },
  { path: '/manus', name: 'ManusChat', component: ManusChat }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;

