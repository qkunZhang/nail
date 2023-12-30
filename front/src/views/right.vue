<template> 
    <aside>
        <button @click="a">test</button>
    </aside>
</template>

<script setup lang="ts">
    import api from "../api/request"

    function a(){
        api({ url: "/getUserById?id=1", method: "get" }).then((res: any) => {console.log(res)})
   	}
</script>

<style scoped>
    aside{
        background-color: aqua;
    }
</style>
