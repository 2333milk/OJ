<script setup lang="ts">

import { Message } from '@arco-design/web-vue';
import { QuestionControllerService, type QuestionQueryRequest, type QuestionVO } from '@/api';
import { onMounted, ref, watchEffect } from 'vue';
import { useRouter } from 'vue-router';
import moment from 'moment';
const total = ref();
const dataList = ref([]);
const searchParams = ref<QuestionQueryRequest>({
    title: '',
    tags: [],
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
watchEffect(()=>{
    loadData();
});
onMounted(() => {
    loadData();
});

const router = useRouter();

/**
 * 跳转做题页面
 */
const goQuestionSolve = (question: QuestionVO) => {
    router.push({
        path: `/submit/question/${question.id}`,
    })
}

const onPageChange = (page: number) => {
    searchParams.value = {
        ...searchParams.value,
        current:page,
    }
}
const columns = [{
    title: '项目名称',
    dataIndex: 'title',
}, {
    title: '标签',
    slotName: 'tags',
}, {
    title: '通过率',
    slotName: 'acceptedRate',
}, {
    title: '创建时间',
    slotName: 'createTime',
}, {
    title: '操作',
    slotName: 'optional',
}]

/**
 * 确认提交，重载数据
 */
const handleSubmit = () => {
    searchParams.value = {
        ...searchParams.value,
        current:1,
    }
};
</script>

<template>
    <div id="listQuestion">
        <a-form :model="searchParams" layout="inline" @submit="handleSubmit">
            <a-form-item field="title" label="题目标题">
                <a-input v-model="searchParams.title" placeholder="请输入题目标题"  style="min-width: 240px;"/>
            </a-form-item>
            <a-form-item field="tags" label="题目标签" >
                <a-input-tag v-model="searchParams.tags" placeholder="输入题目标签" style="min-width: 240px;"/>
            </a-form-item>
            <a-form-item>
                <a-button type="primary" html-type="submit">提交</a-button>
            </a-form-item>
        </a-form>
        <a-divider :size="0"/>
        <a-table :columns="columns" :data="dataList" style="margin-top: 30px" @page-change="onPageChange" :pagination="{
            showTotal: true,
            pageSize: searchParams.pageSize,
            current: searchParams.current,
            total: total
        }">
            <template #tags="{ record }">
                <a-space wrap>
                    <a-tag v-for="(tag, index) of record.tags" :key="index" color="blue">{{ tag }}</a-tag>
                </a-space>
            </template>
            <template #acceptedRate="{ record }">
                {{ `${record.submitNum ? record.acceptedNum / record.submitNum :
            '0'}%(${record.acceptedNum}/${record.submitNum})` }}
            </template>
            <template #createTime="{ record }">{{ moment(record.createTime).format('YYYY-MM-DD') }}</template>
            <template #optional="{ record }">
                <a-button type="primary" @click="goQuestionSolve(record)">开始答题</a-button>
            </template>
        </a-table>
    </div>

</template>

<style scoped>
#listQuestion {
    min-width: 1280px;
    margin: 0 auto;
}
</style>
