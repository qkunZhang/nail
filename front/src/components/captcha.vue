<template>
    <div>
        <canvas id="captchaImg" width="256" height="100"></canvas>
        <input type="text" placeholder="6位验证码" maxlength="6" v-model="submittedCaptcha">     
        <button type="submit" @click="goToInvitationCode()">进行验证</button>
        <label>要求：<br>1、将汉字转为阿拉伯数字<br>2、字母不限大小写。</label>
    </div>
</template>

<script setup lang="ts">
    import { ref,onMounted} from 'vue'
    import Captcha from 'captcha-mini'
    import {useSignUpStore} from '../stores/signUpStore.ts'
    import {useSignInStore} from '../stores/signInStore.ts'
    


    const props = defineProps({
         where: String,
    });

    let submittedCaptcha = ref("")
    let CorrectCaptcha:string = ""
    const signUpStore = useSignUpStore()
    const signInStore = useSignInStore()



    onMounted(()=>{generateCaptcha();})

    function goToInvitationCode(){
        if(submittedCaptcha.value.toLowerCase() != CorrectCaptcha){
            generateCaptcha()
            alert("验证码错误！")
        }else{
            if(props.where === "sign-in"){
                signInStore.isCaptchaCorrect = true
            }else if(props.where === "sign-up"){
                signUpStore.isCaptchaCorrect= true
            }else{
                console.log(props.where)
            }
        }
    }

    function generateCaptcha(){
        new Captcha({
            lineWidth: 1,       //线条宽度
            lineNum: 15,        //线条数量
            dotR: 1,            //点的半径
            dotNum: 300,        //点的数量
            preGroundColor: [200, 255],     //前景色区间
            backGroundColor: [0, 100],      //背景色区间
            fontSize: 25,               //字体大小
            fontFamily: [],             //字体类型
            fontStyle: 'stroke',        //字体绘制方法，有fill和stroke
            content: 'AEHKMNPRTUWXYaemnprsuwxy234零四五六七八九壹贰叁肆伍陆柒捌玖',  //验证码内容
            length: 6    //验证码长度
        })
        .draw(document.querySelector('#captchaImg'), (rawCaptcha: string) => {
            CorrectCaptcha = convertChineseNumberToArabic(rawCaptcha.toLowerCase())
        });
    }

    function convertChineseNumberToArabic(text: string): string {
        const chineseNumberMap: {[key: string]: number} = {
            一: 1,
            二: 2,
            三: 3,
            四: 4,
            五: 5,
            六: 6,
            七: 7,
            八: 8,
            九: 9,
            零: 0,
            壹: 1,
            贰: 2,
            叁: 3,
            肆: 4,
            伍: 5,
            陆: 6,
            柒: 7,
            捌: 8,
            玖: 9};
        let result = "";
        for (let i = 0; i < text.length; i++) {
            const char = text[i];
            if (chineseNumberMap[char] !== undefined) {
                result += chineseNumberMap[char];
            } else {
                result += char;
            }
        }
        return result;
    }
    
</script>

<style scoped>
    #captchaImg{
        user-select: none;
    }
   
</style>
../utils/router