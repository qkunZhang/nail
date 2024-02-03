<template>
  <div>
      <Draggable v-model="treeData" treeLine :defaultOpen="false" disableDrag disableDrop>
        <template v-slot:default="{ node, stat }">
          <span @click="stat.open = !stat.open" v-if="stat.children.length">
          {{ stat.open ? '-' : '+' }}
        </span>
          <span @click="handleNodeClick(node)">{{ node.text }}</span>
        </template>
      </Draggable>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Draggable } from '@he-tree/vue'
import '@he-tree/vue/style/default.css'
import '@he-tree/vue/style/material-design.css'

const treeData = ref([
  {
    text: 'Projects',
    id: 1,
    children: [
      {
        text: 'Frontend',
        id: 2,
        children: [
          {
            text: 'Vue',
            id: 3,
            children: [
              {
                text: 'Nuxt',
                id: 4,
              },
            ],
          },
          {
            text: 'React',
            id: 5,
            children: [
              {
                text: 'Next',
                id: 6,
              },
            ],
          },
          {
            text: 'Angular',
            id: 7,
          },
        ],
      },
      {
        text: 'Backend',
        id: 8,
      },
    ],
  },
  { text: 'Photos', id: 9 },
  { text: 'Videos', id: 10 },
]);

const router = useRouter();




</script>