import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useSignUpStore = defineStore('signUpStore', () => {
  let isCaptchaCorrect = ref(false)
  let canSignUp = ref(false)
  return { isCaptchaCorrect ,canSignUp}
})