<template>
    <div>
        <Draggable v-model="treeData" treeLine :defaultOpen="false" disableDrag disableDrop  ref="tree">
            <template v-slot:default="{ node, stat }">
                <span @click="stat.open = !stat.open;console.log(stat,node)" v-if="stat.children.length">
                    {{ stat.open ? '-' : '+' }}
                </span>
                <span @click="handleNodeClick(node)">
                    {{ node.text }}
                </span>
                <button @click="test(stat)">test</button>
            </template>
        </Draggable>
    </div>
    
</template>

<script setup lang="ts">
import { Draggable } from '@he-tree/vue'
import '@he-tree/vue/style/default.css'
import '@he-tree/vue/style/material-design.css'
import router from '../../utils/router'
import { ref } from 'vue';

const tree = ref(null)

function test(stat){
  console.log(tree.value)
  tree.value.addMulti(
          [{ text: 'addMulti1' }, { text: 'addMulti2' }],
          stat,
          0
        )
}

const treeData = ref([
  {
    text: '文章',
    children: [
      {
        text: '文章1',

      }, 
      {
        text: '文章2',
      },   
    ]
  },
  {
    text: '问答',
  },
  {
    text: '资源',
  },
]);

function handleNodeClick(_node: { text: string; }) {
  
}
</script>

<style scoped>

</style>
