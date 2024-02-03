/// <reference types="vite/client" />
declare module 'captcha-mini';
declare module 'crypto-js'
declare module 'eccrypto'
declare module '*.vue' {
    import type { DefineComponent } from 'vue'
    const component: DefineComponent<{}, {}, any>
    export default component
  }
