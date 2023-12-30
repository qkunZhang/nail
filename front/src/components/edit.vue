<template>
    <div class="editorBox">
        <div class="editArea">
            <textarea class="editor" placeholder="在此处编辑您的文章"></textarea>
        </div>
        <!-- <div class="displayArea">
            nn
        </div> -->
    </div>
</template>

<script setup lang="ts">
    
</script>

<style scoped>
    .editorBox{
        width: 100%;
        height: 100%;
        display: flex;
    }
    .editArea{
        flex-grow: 1;
    }
    .editor{
        width: 100%;
        height: 100%;
        box-sizing: border-box;
        resize: none;
    }

   
</style>
