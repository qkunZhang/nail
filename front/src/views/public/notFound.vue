<template> 
   404
</template>

<script setup lang="ts">
   import { onMounted } from 'vue';
   import { useRoute } from 'vue-router';
    onMounted(()=>{
    const route = useRoute();
    console.log("path",route.path); // 输出当前路径
    console.log("params",route.params); // 输出路由参数
    })
</script>

<style scoped>

</style>
