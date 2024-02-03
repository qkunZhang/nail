import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useResetPasswordStore = defineStore('resetPasswordStore', () => {
  let isResetPassword = ref(false)
  return {  isResetPassword}
})