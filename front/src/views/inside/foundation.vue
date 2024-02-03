<template>
    <nav>
        <span @click="goWhere('/home')">首页</span>
        <navigation></navigation>
        <navigation></navigation>
        <navigation></navigation>
        <span @click="goWhere('/mine')">我的</span>
    </nav>
    <main>
        <RouterView/>
    </main>
    <aside>
        侧边栏
    </aside>
</template>

<script setup lang="ts">
import  navigation from '../inside/navigation.vue'
import router from '../../utils/router';
function goWhere(to:string) {
    router.push(to)
}



    
</script>

<style>

</style>
