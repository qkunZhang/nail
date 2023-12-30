import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'

const pinia = createPinia()
import axios from './api/request'
const app = createApp(App)
.use(pinia)
.provide("$axios", axios)
.mount('#app')

// app.config.globalProperties.$axios = axios; //配置axios的全局引用

