<script setup lang="ts">

import { Message } from '@arco-design/web-vue';
import { QuestionControllerService, type JudgeCase, type JudgeConfig, type QuestionAddRequest, type QuestionUpdateRequest, type QuestionVO } from '@/api';
import { onMounted, ref, watchEffect } from 'vue';
import { useRouter } from 'vue-router';
import { reactive } from 'vue';
import MdEditer from '@/components/MdEditer.vue';
const total = ref();
const dataList = ref([] as QuestionVO[]);
const searchParams = ref({
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
    dataList.value = res.data.records as QuestionVO[];
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
    children: [
      {
        title: '时间限制',
        dataIndex: 'judgeConfig.timeLimit'
      }, {
        title: '空间限制',
        dataIndex: 'judgeConfig.memoryLimit'
      }, {
        title: '堆栈限制',
        dataIndex: 'judgeConfig.stackLimit'
      }]
  },
  {
    title: '操作',
    slotName: 'optional'
  },
];


const visible = ref(false);
const statusChange = ref(1);
const form = reactive<QuestionUpdateRequest>({
  judgeCase: [] as JudgeCase[],
  judgeConfig: {} as JudgeConfig,
});

const onChangeAnswer = (v: string) => {
  form.answer = v;
};

const onChangeContext = (v: string) => {
  form.content = v;
};
const handleOk = async () => {
  if (statusChange.value === 1) {
    const res = await QuestionControllerService.addQuestionUsingPost(form);
    if (res.code == 0) {
      Message.success("创建成功");
    } else {
      Message.error("创建失败," + res.message);
    }
  } else {
    const res = await QuestionControllerService.updateQuestionUsingPost(form);
    if (res.code == 0) {
      Message.success("修改成功");
    } else {
      Message.error("修改失败," + res.message);
    }
  }
  loadData();
  visible.value = false;
};
const handleAdd = () => {
  if (form.judgeCase) {
    form.judgeCase.push({} as JudgeCase)
  }

};
const handleDelete = (index: number) => {
  if (form.judgeCase) {
    form.judgeCase.splice(index, 1);
  }
};
const doAdd = () => {
  form.answer = undefined;
  form.content = undefined;
  form.judgeCase = [] as JudgeCase[];
  form.judgeConfig = {} as JudgeConfig;
  form.tags = undefined;
  form.title = undefined;
  statusChange.value = 1;
  visible.value = true;
}
const doEdit = (question: QuestionVO) => {
  form.id = question.id;
  form.answer = question.answer;
  form.content = question.content;
  form.judgeCase = question.judgeCase;
  form.judgeConfig = question.judgeConfig;
  form.tags = question.tags;
  form.title = question.title;
  statusChange.value = 2;
  visible.value = true;
}
const rules = {
  title: [
    {
      required: true,
      message: '未填写标题',
    },
  ],
  tags: [
    {
      required: true,
      message: '未填写标签',
    },
  ],
  answer: [
    {
      required: true,
      message: '未填写答案',
    },
  ],
  content: [
    {
      required: true,
      message: '未填写内容',
    },
  ],
  judgeConfig: [
    {
      required: true,
      message: '未填写判题配置',
    },
  ],
  judgeCase: [
    {
      required: true,
      message: '未填写判题用例',
    },
  ],
}
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
  <div id="manageQuestion">
    <a-form :model="searchParams" layout="inline" @submit="handleSubmit">
      <a-form-item field="title" label="题目标题">
        <a-input v-model="searchParams.title" placeholder="请输入题目标题" style="min-width: 240px;" />
      </a-form-item>
      <a-form-item field="tags" label="题目标签">
        <a-input-tag v-model="searchParams.tags" placeholder="输入题目标签" style="min-width: 240px;" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
        <a-button type="primary" @click="doAdd()" style="margin-left: 500px;">新增题目</a-button>
      </a-form-item>
    </a-form>
    <a-divider :size="0" />

    <a-table style="margin-top: 30px" :columns="columns" :data="dataList" @page-change="onPageChange" :pagination="{
      showTotal: true,
      pageSize: searchParams.pageSize,
      current: searchParams.current,
      total: parseInt(total, 10)
    }">
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doEdit(record)">修改</a-button>
          <a-button type="primary" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
    </a-table>
    <a-drawer :width="750" v-model:visible="visible">
      <template #title>
        <div v-if="statusChange === 1">新建考试 </div>
        <div v-else-if="statusChange === 2">修改考试 </div>
        <div v-else>删除考试</div>
      </template>
      <a-form :model="form" :style="{ width: '600px' }" :rules="rules" @submit-success="handleOk">
        <a-form-item field="title" label="题目标题">
          <a-input v-model="form.title" placeholder="请输入题目标题" />
        </a-form-item>
        <a-form-item field="tags" label="题目标签">
          <a-input-tag v-model="form.tags" placeholder="输入题目标签" />
        </a-form-item>
        <a-form-item field="answer" tooltip="请输入答案" label="答案">
          <MdEditer class="mdEditer" v-model="form.answer" :value="form.answer" :mode="'split'"
            :handle-change="onChangeAnswer" placeholder="请输入" />
        </a-form-item>
        <a-form-item field="content" tooltip="请输入内容" label="内容">
          <MdEditer class="mdEditer" v-model="form.content" :value="form.content" :mode="'split'"
            :handle-change="onChangeContext" placeholder="please enter your post..." />
        </a-form-item>
        <a-form-item field="judgeConfig" label="判题配置" :content-flex="false" :merge-props="false"
          v-if="form.judgeConfig">
          <a-space direction="vertical" fill>
            <a-form-item field="judgeConfig.timeLimit" label="时间限制 ms">
              <a-input-number v-model="form.judgeConfig.timeLimit" placeholder="请输入时间限制" mode="button" size="large"
                class="input-demo" />
            </a-form-item>
            <a-form-item field="judgeConfig.memoryLimit" label="内存限制 kb">
              <a-input-number v-model="form.judgeConfig.memoryLimit" placeholder="请输入内存限制" mode="button" size="large"
                class="input-demo" />
            </a-form-item>
            <a-form-item field="judgeConfig.stackLimit" label="堆栈限制">
              <a-input-number v-model="form.judgeConfig.stackLimit" placeholder="请输入堆栈限制" mode="button" size="large"
                class="input-demo" />
            </a-form-item>

          </a-space>
        </a-form-item>
        <a-form-item field="judgeCase" label="测试用例" :content-flex="false" :merge-props="false">
          <a-form-item v-for="(judgeCaseItem, index) of form.judgeCase" :field="`用例[${index}].value`"
            :label="`用例 ${index}`" :key="index">
            <a-space direction="vertical" fill>
              <a-form-item field="judgeConfig.timeLimit" label="输入">
                <a-input v-model="judgeCaseItem.input" :style="{ width: '300px' }" />
              </a-form-item>
              <a-form-item field="judgeConfig.memoryLimit" label="输出">
                <a-input v-model="judgeCaseItem.output" :style="{ width: '300px' }" />
              </a-form-item>
              <a-button status="danger" @click="handleDelete(index)" :style="{ marginLeft: '10px' }">删除</a-button>
            </a-space>
          </a-form-item>
          <div>
            <a-button type="outline" status="success" @click="handleAdd">新增测试用例</a-button>
          </div>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" :style="{ width: '200px' }">提交</a-button>
        </a-form-item>
      </a-form>
      <template #footer>
        <div></div>
      </template>
    </a-drawer>
  </div>

</template>

<style scoped>
#manageQuestion {
  min-width: 1280px;
  margin: 0 auto;
}
</style>
