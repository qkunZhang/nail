<template> 
    <captcha v-if = "!signUpStore.isCaptchaCorrect" where="sign-up"></captcha>
    <userInfoForm v-if = "signUpStore.isCaptchaCorrect"></userInfoForm>
    <RouterLink to="/login">有账号，去登录</RouterLink>
</template>

<script setup lang="ts">
    // import { ref ,onMounted} from 'vue'
    // import http from '../../utils/axios'
    import captcha from '@/components/captcha.vue'
    import userInfoForm from '../../components/userInfoForm.vue'
    import {useSignUpStore} from '../../stores/signUpStore.ts'

    const signUpStore = useSignUpStore()


</script>

<style scoped>

</style>