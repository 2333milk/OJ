<script setup lang="ts">
import Basiclayout from '@/layouts/BasicLayout.vue';
import { onMounted } from 'vue';
import { useRoute } from 'vue-router';
const route = useRoute();
const doinit = () => {
  console.log("oj系统，启动！");
};

onMounted(() => {
  doinit();
});


</script>

<template>
  <div id="app">
    <RouterView />
  </div>
</template>

<style scoped>
#app {
  margin: 0px;
  padding: 0px;
  width: 100%;
}
</style>
