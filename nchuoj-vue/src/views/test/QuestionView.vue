<script setup lang="ts">
import MdEditer from '@/components/MdEditer.vue';
import { ref } from 'vue';
const value = ref('');
const onChange = (v: string) => {
    value.value = v;
};

</script>

<template>
    <div class="Question">
        this is question view
        <MdEditer :value="value" :handle-change="onChange" />
    </div>
</template>

<style scoped></style>