<template>  
    <left/>
    <middle/>
    <right/>
</template>

<script setup lang="ts">
    import left from './views/left.vue';
    import middle from './views/middle.vue';
    import right from './views/right.vue';
</script>

<style>
    html,body{
        margin: 0%;
        padding: 0%;
        height: 100%;
    }

    #app{
        display: flex;
        height: 100%;
    }
</style>
