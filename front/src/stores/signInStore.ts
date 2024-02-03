import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useSignInStore = defineStore('signInStore', () => {
  let isCaptchaCorrect = ref(false)
  let canSignIn = ref(false)
  return { isCaptchaCorrect ,canSignIn,}
})