<script setup lang="ts">

import {Message} from '@arco-design/web-vue';
import {
  QuestionControllerService,
  type QuestionSubmitQueryRequest,
} from '@/api';
import {onMounted, ref, watchEffect} from 'vue';
import { useRouter} from 'vue-router';
import moment from 'moment';
const total = ref();
const dataList = ref([]);
const searchParams = ref<QuestionSubmitQueryRequest>({
  // status:undefined,
  language:undefined,
  pageSize: 5,
  current: 1,
})


/**
 * 加载表格数据
 */
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionSubmitVoByPageUsingPost(searchParams.value);
  if (res.code == 0) {
    dataList.value = res.data.records;
    total.value= res.data.total;
    console.log(dataList.value);
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

const router = useRouter();


const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  }
}
// code?: string;
// id?: number;
// judgeInfo?: JudgeInfo;
// language?: string;
// questionId?: number;
// questionVO?: QuestionVO;
// status?: number;
// userVO?: UserVO;
const columns = [{
  title: '题目标题',
  dataIndex: 'questionVO.title',
}, {
  title: '提交用户',
  dataIndex: 'userVO.userName',
},{
  title: '判题信息',
  dataIndex: 'judgeInfo.message',
},{
  title: '所用时长',
  dataIndex: 'judgeInfo.time',
},{
  title: '所用空间',
  dataIndex: 'judgeInfo.memory',
},{
  title: '判题状态',
  slotName: 'status',
}, {
  title: '编程语言',
  dataIndex: 'language',
}, {
  title: '提交时间',
  slotName: 'createTime',
}
]

/**
 * 确认提交，重载数据
 */
const handleSubmit = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  }
};
</script>

<template>
  <div id="listQuestion">
    <a-form :model="searchParams" layout="inline" @submit="handleSubmit">
      <a-form-item field="language" labal="编程语言">
        <a-select  v-model="searchParams.language" placeholder="请选择编程语言">
          <a-option>java</a-option>
<!--          <a-option>c++</a-option>-->
<!--          <a-option>go</a-option>-->
        </a-select>
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
      <template #status="{ record }">
        {{record.status==0?"等待中":record.status==1?"判题中":record.status==2?"成功":"失败"}}
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format('YYYY-MM-DD h:mm:ss') }}
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
