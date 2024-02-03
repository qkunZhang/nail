<template>
    <div>
        <input type="text" placeholder="邀请码" maxlength="128" @input="formatCheckRealTime" v-model="invitationCodeInput" :style="{ borderColor: invitationCodeBorderColor, outlineColor: invitationCodeOutlineColor }">
        <input type="text" placeholder="用户名" maxlength="16" @input="formatCheckRealTime" v-model="usernameInput" :style="{ borderColor: usernameBorderColor, outlineColor: usernameOutlineColor }"><label></label>
        <input type="password" placeholder="密码" maxlength="16" @input="formatCheckRealTime" v-model="passwordInput" :style="{ borderColor: passwordBorderColor, outlineColor: passwordOutlineColor }">
        <input type="email" placeholder="邮箱地址" maxlength="320" @input="formatCheckRealTime" v-model="emailInput" :style="{ borderColor: emailBorderColor, outlineColor: emailOutlineColor }">
        <input type="text" placeholder="邮箱验证码" maxlength="6" @input="formatCheckRealTime" v-model="verificationCodeInput" :style="{ borderColor: verificationCodeBorderColor, outlineColor: verificationCodeOutlineColor }">
        <button @click="getEmailVerificationCode" :disabled="isButtonDisabled">获取邮箱验证码</button>
        <button type="submit" @click="submit">注册</button> 
    </div>
</template>

<script setup lang="ts">
    import { ref,onMounted} from 'vue'
    import http from '../utils/axios';
    import { encryptSHA3,encryptXOR} from '../utils/crypto.ts';
import router from '../utils/router';


    // const signUpStore = useSignUpStore()

    let isButtonDisabled = ref(false)

    let isInvitationCodeValid:boolean = false
    let isUsernameValid:boolean = false
    let isPasswordValid:boolean = false
    let isEmailValid:boolean = false
    let isVerificationCodeValid:boolean = false

    let invitationCodeInput = ref("")
    let usernameInput = ref("")
    let passwordInput = ref("")
    let emailInput = ref("")
    let verificationCodeInput = ref("")

    let invitationCodeBorderColor = ref("")
    let usernameBorderColor = ref("")
    let passwordBorderColor = ref("")
    let emailBorderColor = ref("")
    let verificationCodeBorderColor = ref("")

    let invitationCodeOutlineColor = ref("")
    let usernameOutlineColor = ref("")
    let passwordOutlineColor = ref("")
    let emailOutlineColor = ref("")
    let verificationCodeOutlineColor = ref("")

    onMounted(()=>{
        formatCheckRealTime()
    })

    function formatCheckRealTime(){
        //邀请码只能输入字母、数字、中划线和下划线,且只能是64位
        if(invitationCodeInput.value===""){
            invitationCodeBorderColor.value = 'black';
            invitationCodeOutlineColor.value = 'black';
        }else if(!/^[a-zA-Z0-9_-]{64}$/.test(invitationCodeInput.value)) {
            invitationCodeBorderColor.value = 'red';
            invitationCodeOutlineColor.value = 'red';
        }else{
            invitationCodeBorderColor.value = 'green';
            invitationCodeOutlineColor.value = 'green';
        }


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
        isInvitationCodeValid = /^[a-zA-Z0-9_-]{64}$/.test(invitationCodeInput.value)
        isUsernameValid = /^[a-zA-Z0-9_]+$/.test(usernameInput.value)
        isPasswordValid = /^[a-zA-Z0-9_]+$/.test(passwordInput.value)
        isEmailValid = /^(?=.{1,64}@.{4,255}$)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,63}/.test(emailInput.value)
        isVerificationCodeValid = /^(?:[a-zA-Z0-9]+)?$/.test(verificationCodeInput.value)
        console.log(invitationCodeInput.value,isInvitationCodeValid)
        if(!isInvitationCodeValid){
            alert("错误格式的邀请码")
            return false
        }

        if(!isUsernameValid){
            alert("错误格式的用户名")
            return false
        }

        if(!isPasswordValid){
            alert("错误格式的密码")
            return false
        }

        if(!isEmailValid){
            alert("错误格式的邮箱")
            return false
        }

        if(!isVerificationCodeValid){
            alert("错误格式的邮箱验证码")
            return false
        }

        return true
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

    
    async function submit(){
        if(formatCheckFinal()){
            let encryptInvitationCode = encryptXOR(invitationCodeInput.value,"INVITATION_CODE")
            let encryptPassword = encryptXOR(encryptSHA3(passwordInput.value),"PASSWORD")
            let encryptEmail = encryptXOR(emailInput.value,"EMAIL")
            let encryptUsername = encryptXOR(usernameInput.value,"USERNAME")
            let encryptVerificationCode = encryptXOR(verificationCodeInput.value,"VERIFICATION_CODE")
            let userInfo = {
                invitationCode: encryptInvitationCode,
                username: encryptUsername,
                password: encryptPassword,
                email: encryptEmail,
                verificationCode: encryptVerificationCode
            }
            http.post("/user/sign-up",userInfo)
            .then(res => {
                console.log(res)
                if(res.data===true&&res.code===0){
                    alert("注册成功")
                    router.push('/sign-in')
                }else{
                    alert("注册失败\n原因："+res.msg)
                }
            }).catch(err =>{
                console.log(err)
            })
        }
    }
    
    
</script>

<style scoped>
    
</style>
