<template>
    <captcha v-if="!signInStore.isCaptchaCorrect" where="sign-in"></captcha>
    <div  v-if="signInStore.isCaptchaCorrect">
        <input type="text" placeholder="用户名" maxlength="16" @input="formatCheckRealTime" v-model="usernameInput" :style="{ borderColor: usernameBorderColor, outlineColor: usernameOutlineColor }"><label></label>
        <input type="password" placeholder="密码" maxlength="16" @input="formatCheckRealTime" v-model="passwordInput" :style="{ borderColor: passwordBorderColor, outlineColor: passwordOutlineColor }">
        <input type="email" placeholder="邮箱地址" maxlength="320" @input="formatCheckRealTime" v-model="emailInput" :style="{ borderColor: emailBorderColor, outlineColor: emailOutlineColor }" v-if="resetPasswordStore.isResetPassword">
        <input type="text" placeholder="邮箱验证码" maxlength="6" @input="formatCheckRealTime" v-model="verificationCodeInput" :style="{ borderColor: verificationCodeBorderColor, outlineColor: verificationCodeOutlineColor }" v-if="resetPasswordStore.isResetPassword"> 
        <button @click="getEmailVerificationCode" :disabled="isButtonDisabled" v-if="resetPasswordStore.isResetPassword">获取邮箱验证码</button>
        <button type="submit" @click="login" v-if="!resetPasswordStore.isResetPassword">登录</button>
        <button type="submit" @click="resetPassword" v-if="resetPasswordStore.isResetPassword">重置密码</button>
        <a @click="toResetPassword" v-if="!resetPasswordStore.isResetPassword" style="color: red;">忘记密码</a>
        <a @click="toSignIn" v-if="resetPasswordStore.isResetPassword" style="color: red;">返回登录</a>
        
    </div>
    <RouterLink to="/register">无账号，去注册</RouterLink>
</template>

<script setup lang="ts">
    import { ref ,onMounted} from 'vue'
    import http from '../../utils/axios'
    import captcha from '../../components/captcha.vue';
    import { useSignInStore } from '../../stores/signInStore';
    import { encryptSHA3,encryptXOR} from '../../utils/crypto'
    import localforage from 'localforage';
    import router from '../../utils/router';
    import { useResetPasswordStore } from '../../stores/resetPasswordStore';


    const signInStore = useSignInStore()
    const resetPasswordStore = useResetPasswordStore()

    
    let isButtonDisabled = ref(false)

    let isUsernameValid:boolean = false
    let isPasswordValid:boolean = false
    let isEmailValid:boolean = false
    let isVerificationCodeValid:boolean = false


    let usernameInput = ref("")
    let passwordInput = ref("")
    let emailInput = ref("")
    let verificationCodeInput = ref("")


    let usernameBorderColor = ref("")
    let passwordBorderColor = ref("")
    let emailBorderColor = ref("")
    let verificationCodeBorderColor = ref("")

    let usernameOutlineColor = ref("")
    let passwordOutlineColor = ref("")
    let emailOutlineColor = ref("")
    let verificationCodeOutlineColor = ref("")

    
    onMounted(()=>{
        formatCheckRealTime()
    })

    function formatCheckRealTime(){
         // 用户名和密码限制输入为字母、数字和下划线
        if(usernameInput.value===""){
            usernameBorderColor.value = 'black';
            usernameOutlineColor.value = 'black';
        }else if (!/^[a-zA-Z0-9_]*$/.test(usernameInput.value)) {
            usernameBorderColor.value = 'red';
            usernameOutlineColor.value = 'red';
        }else{
            usernameBorderColor.value = 'green';
            usernameOutlineColor.value = 'green';
        }

        if(passwordInput.value===""){
            passwordBorderColor.value = "black";
            passwordOutlineColor.value = "black";
        }else if (!/^[a-zA-Z0-9_]*$/.test(passwordInput.value)) {
            passwordBorderColor.value = 'red';
            passwordOutlineColor.value = 'red';
        }else{
            passwordBorderColor.value = 'green';
            passwordOutlineColor.value = 'green';
        }

        // 邮箱只能XXXXXX@xxx.XXX格式
        if(emailInput.value===""){
            emailBorderColor.value = "black";
            emailOutlineColor.value = "black";
        }else if(!/^(?=.{1,64}@.{4,255}$)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,63}$/.test(emailInput.value)){
            emailBorderColor.value = 'red';
            emailOutlineColor.value = 'red';
        }else{
            emailBorderColor.value = 'green';
            emailOutlineColor.value = 'green';
        }
        

        // 邮箱验证码限制输入为字母和数字
        if(verificationCodeInput.value===""){
            verificationCodeBorderColor.value = "black";
            verificationCodeOutlineColor.value = "black";
        }else if (!/^[a-zA-Z0-9]*$/.test(verificationCodeInput.value)) {
            verificationCodeBorderColor.value = 'red';
            verificationCodeOutlineColor.value = 'red';
        } else {
            verificationCodeBorderColor.value = 'green';
            verificationCodeOutlineColor.value = 'green';
        }

    }

    function formatCheckFinal():boolean{
        isUsernameValid = /^[a-zA-Z0-9_]+$/.test(usernameInput.value)
        isPasswordValid = /^[a-zA-Z0-9_]+$/.test(passwordInput.value)
        isEmailValid = /^(?=.{1,64}@.{4,255}$)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,63}/.test(emailInput.value)
        isVerificationCodeValid = /^(?:[a-zA-Z0-9]+)?$/.test(verificationCodeInput.value)

        if(!isUsernameValid){
            alert("错误格式的用户名")
            return false
        }

        if(!isPasswordValid){
            alert("错误格式的密码")
            return false
        }


        return true
    }

    function formatCheckFinalWithEmail(){

        if(!formatCheckFinal()){
            return false;
        }
        
        if(!isEmailValid){
            alert("错误格式的邮箱")
            return false
        }

        if(!isVerificationCodeValid){
            alert("错误格式的邮箱验证码")
            return false
        }

        return true;

    }

    function getEmailVerificationCode(){
        if(/^(?=.{1,64}@.{4,255}$)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,63}/.test(emailInput.value)){
            http.post("/email/send-verification-code",emailInput.value)
            .then(res => {
                console.log(res)
                isButtonDisabled.value = true
                setTimeout(() => {
                    isButtonDisabled.value = false; // 65s后恢复可点击状态
                }, 65000);
                alert("验证码已发往您填入的邮箱，有效时间为5分钟。\n如果您无法识别验证码，可以在约一分钟后再次请求。")
            }).catch(err =>{
                console.log(err)
            })
        }else{
            alert("邮箱格式错误")
        }
    }


    function login(){
        if(formatCheckFinal()){
            let encryptPassword = encryptXOR(encryptSHA3(passwordInput.value),"PASSWORD")
            let encryptUsername = encryptXOR(usernameInput.value,"USERNAME")
            let userInfo = {
                username: encryptUsername,
                password: encryptPassword,
            }
            console.log(userInfo)
            http.post("/user/sign-in",userInfo)
            .then(res => {
                if(res.code ===0){
                    alert("登录成功")
                    
                    const authDB = localforage.createInstance({name: "authDB"});
                    authDB.setItem("refreshJWT",res.data.refreshJWT)
                    authDB.setItem("accessJWT",res.data.accessJWT)
                    router.push('/home')
                }else{
                    alert(res.msg)
                }
            }).catch(err =>{
                console.log(err)
            })
        }
    }

    function toResetPassword(){
        resetPasswordStore.isResetPassword = true
    }
    function toSignIn(){
        resetPasswordStore.isResetPassword = false
    }

    function resetPassword(){
        if(formatCheckFinalWithEmail()){
            let encryptPassword = encryptXOR(encryptSHA3(passwordInput.value),"PASSWORD")
            let encryptUsername = encryptXOR(usernameInput.value,"USERNAME")
            let encryptEmail = encryptXOR(emailInput.value,"EMAIL")
            let encryptVerificationCode = encryptXOR(verificationCodeInput.value,"VERIFICATION_CODE")

            let userInfo = {
                username: encryptUsername,
                password: encryptPassword,
                email: encryptEmail,
                verificationCode: encryptVerificationCode
            }
            console.log(userInfo)
            http.post("/user/password-reset",userInfo)
            .then(res => {
                if(res.code!=0 || res.data==false){
                    alert(res.msg)
                }else{
                    alert("重置密码成功")
                }
            }).catch(err =>{
                alert("重置密码失败")
            })
        }
    }

    
</script>

<style scoped>

</style>
