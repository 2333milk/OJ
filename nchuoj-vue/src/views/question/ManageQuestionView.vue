<script setup lang="ts">

import { Message } from '@arco-design/web-vue';
import { QuestionControllerService, type QuestionVO } from '@/api';
import { onMounted, ref, watchEffect } from 'vue';
import { useRouter } from 'vue-router';

const total = ref();
const dataList = ref([]);
const searchParams = ref({
    pageSize: 5,
    current: 1,
})

/**
 * 加载表格数据
 */
const loadData = async () => {
    const res = await QuestionControllerService.listQuestionVoByPageUsingPost(searchParams.value);
    if (res.code == 0) {
        dataList.value = res.data.records;
        total.value = res.data.total;
    } else {
        Message.error("加载数据错误," + res.message);
    }
}
watchEffect(() => {
    loadData();
});
onMounted(() => {
    loadData();
});

const doDelete = async (question: QuestionVO) => {
    const res = await QuestionControllerService.deleteQuestionUsingPost({ id: question.id });
    if (res.code == 0) {
        Message.success("删除成功");
        loadData();
    } else {
        Message.error("删除失败，" + res.message);
    }
}
const router = useRouter();
const doEdit = (question: QuestionVO) => {
    router.push({
        path: '/submit/editQuestion',
        query: {
            id: question.id,
        },
    })
}

const onPageChange = (page: number) => {
    searchParams.value = {
        ...searchParams.value,
        current: page,
    }
}
const columns = [
  {
    title: '标题',
    dataIndex: 'title',
  },
  {
    title: '标签',
    dataIndex: 'tags',
  },
  {
    title: '判题配置',
    dataIndex: 'judgeConfig',
    children:[
        {
      title: '时间限制',
      dataIndex: 'judgeConfig.timeLimit'
    },{
      title: '空间限制',
      dataIndex: 'judgeConfig.memoryLimit'
    },{
        title: '堆栈限制',
        dataIndex: 'judgeConfig.stackLimit'
      }]
  },
  {
    title: '操作',
    slotName: 'optional'
  },
];
const doAdd = ()=>{
  router.push({
    path:'/submit/addQuestion'
  })
}
</script>

<template>
    <div id="manageQuestion">
      <a-button type="primary" @click="doAdd()">新增题目</a-button>
      <a-table style="margin-top: 30px" :columns="columns" :data="dataList" @page-change="onPageChange" :pagination="{
            showTotal: true,
            pageSize: searchParams.pageSize,
            current: searchParams.current,
            total: total
        }">
        <template #optional="{ record }">
          <a-space>
            <a-button type="primary" @click="doEdit(record)">修改</a-button>
            <a-button type="primary" @click="doDelete(record)">删除</a-button>
          </a-space>
        </template>
      </a-table>
    </div>

</template>

<style scoped>
#manageQuestion {
    min-width: 1280px;
    margin: 0 auto;
}
</style>
