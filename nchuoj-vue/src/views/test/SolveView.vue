<script setup lang="ts">
import { ref } from 'vue';
import CodeEditer from '../../components/CodeEditer.vue'
import * as monaco from 'monaco-editor'
const value = ref('')
const language = ref('javascript')
const editorMounted = (editor: monaco.editor.IStandaloneCodeEditor) => {
  console.log('editor实例加载完成', editor)
}

</script>

<template>
    <div id="Solve">
        <CodeEditer
      v-model="value"
      :language="language"
      width="800px"
      height="500px"
      @editor-mounted="editorMounted"
    ></CodeEditer>
    </div>
</template>
<style scoped>
</style>