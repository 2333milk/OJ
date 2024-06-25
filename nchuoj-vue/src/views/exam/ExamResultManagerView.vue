<script setup lang="ts">
import { type ExamQueryRequest, ExamControllerService, type ExamVO } from '@/api';
import { Message } from '@arco-design/web-vue';
import { ref, watchEffect, onMounted } from 'vue';
import moment from 'moment';
import "moment/dist/locale/zh-cn";
import { reactive } from 'vue';
const searchParams = ref<ExamQueryRequest>({
    title: '',
    pageSize: 5,
    current: 1,
});
const examList = ref([]);
const total = ref();
const exam = reactive<ExamVO>({});

/**
 * 加载表格数据
 */
const loadData = async () => {
    const res = await ExamControllerService.listMyExamVoByPageUsingPost(searchParams.value);
    if (res.code == 0) {
        examList.value = res.data.records;
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


const onPageChange = (page: number) => {
    searchParams.value = {
        ...searchParams.value,
        current: page,
    }
}
const columns = [{
    title: '考试名称',
    dataIndex: 'title',
}, {
    title: '描述',
    dataIndex: 'description',
    ellipsis: true,
    width: 150,
}, {
    title: '开始时间',
    slotName: 'startTime',
}, {
    title: '结束时间',
    slotName: 'endTime',
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
        current: 1,
    }
};
const visible = ref(false);

const handleOk = () => {
    visible.value = false;
};
const handleCancel = () => {
    visible.value = false;
}
const watchScores = async (exam: ExamVO) => {
    exam.id=exam.id;
}

const exportExcel = async (exam: ExamVO) => {
    
}

</script>

<template>
    <div id="examResultManager">
        <a-form :model="searchParams" layout="inline">
            <a-form-item field="title" label="考试标题">
                <a-input v-model="searchParams.title" placeholder="请输入考试标题" style="min-width: 240px;" />
            </a-form-item>
            <a-form-item>
                <a-button type="primary" html-type="submit" @click="handleSubmit">搜索</a-button>
            </a-form-item>
        </a-form>
        <a-divider :size="0" />
        <a-table :columns="columns" :data="examList" style="margin-top: 30px" @page-change="onPageChange" :pagination="{
            showTotal: true,
            pageSize: searchParams.pageSize,
            current: searchParams.current,
            total: parseInt(total, 10)
        }">
            <template #startTime="{ record }">{{ moment(record.startTime).utcOffset(0).format('YYYY-MM-DD HH:mm:ss')
                }}</template>
            <template #endTime="{ record }">{{ moment(record.endTime).utcOffset(0).format('YYYY-MM-DD HH:mm:ss')
                }}</template>
            <template #optional="{ record }">
                <a-space size="medium">
                    <a-button type="primary" @click="watchScores(record)">查看</a-button>
                    <a-button type="primary" @click="exportExcel(record)">导出表格</a-button>
                </a-space>
            </template>
        </a-table>
        <a-modal v-model:visible="visible" @ok="handleOk" @cancel="handleCancel">
            <template #title>
                {{  }}
            </template>
            <ScoreEcharts  :exam_id="exam.id" />
        </a-modal>
    </div>
</template>

<style scoped></style>
