// import axios from 'axios';
//
// const http = axios.create({
//   baseURL: '/api',
//   timeout: 15000
// });
//
// http.interceptors.request.use(
//   (config) => {
//     return config;
//   },
//   (error) => Promise.reject(error)
// );
//
// http.interceptors.response.use(
//   (response) => response.data,
//   (error) => Promise.reject(error)
// );
//
// export default http;
//
import axios from 'axios';

// 根据环境变量设置 API 基础 URL（核心修改）
// 替换原来的 process.env.NODE_ENV
const API_BASE_URL = import.meta.env.PROD
    ? '/api'
    : 'http://localhost:8123/api';
// 创建 Axios 实例，使用动态的基础URL
const http = axios.create({
    baseURL: API_BASE_URL, // 替换原来的固定 /api
    timeout: 15000
});

// 请求拦截器（不变）
http.interceptors.request.use(
    (config) => {
        return config;
    },
    (error) => Promise.reject(error)
);

// 响应拦截器（不变）
http.interceptors.response.use(
    (response) => response.data,
    (error) => Promise.reject(error)
);

export default http;