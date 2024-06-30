<script setup lang="ts">
import * as XLSX from 'xlsx';
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
  const res = await QuestionControllerService.listMyQuestionVoByPageUsingPost(searchParams.value);
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
    slotName: 'tags',
  },
  {
    title: '判题配置',
    dataIndex: 'judgeConfig',
    children: [
      {
        title: '时间限制(ms)',
        dataIndex: 'judgeConfig.timeLimit'
      }, {
        title: '空间限制(Mb)',
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
  form.status = question.status;
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
    current: 1,
  }
};

const file = ref();
const visibleModel = ref(false);
const questionlist = ref();
const listAdd = () => {
  visibleModel.value = true;
}

const handleOkModel = async () => {
  const res = await QuestionControllerService.addListQuestionUsingPost(questionlist.value);
  if (res.code == 0) {
    visibleModel.value = false;
    loadData();
  } else {
    Message.error("导入失败," + res.message);
  }

};
const handleCancelModel = () => {
  visibleModel.value = false;
}
const handleUpload = (file: any) => {
  questionlist.value = undefined;
  const reader = new FileReader();
  reader.onload = (e: ProgressEvent<FileReader>) => {
    const data = new Uint8Array(e.target.result as ArrayBuffer);
    const workbook = XLSX.read(data, { type: 'array' });
    // 假设第一个工作表包含问题数据  
    const worksheetName = workbook.SheetNames[0];
    const worksheet = workbook.Sheets[worksheetName];

    // 将工作表数据转换为 JSON 数组  
    const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 2 }); // 假设第一行是标题行  

    // 将 JSON 数组转换为 QuestionAddRequest 类型的数组  
    const questionsArray: Array<QuestionAddRequest> = jsonData.map(row => {
      // 这里你可能需要根据你的 Excel 文件的列来映射到 QuestionAddRequest 的属性  
      // 假设列的顺序和内容与 QuestionAddRequest 的属性匹配  
      const question: QuestionAddRequest = {
        answer: row.answer,
        content: row.content,
        // 对于 judgeCase 和 judgeConfig，你可能需要额外的逻辑来解析复杂的对象  
        // 例如，如果它们在 Excel 中以 JSON 字符串的形式存储  
        judgeCase: row.judgeCase ? JSON.parse(row.judgeCase) : undefined,
        judgeConfig: row.judgeConfig ? JSON.parse(row.judgeConfig) : undefined,
        tags: row.tags ? JSON.parse(row.tags) : undefined, // 假设 tags 是以逗号分隔的字符串  
        title: row.title,
      };
      return question;
    });
    // 更新 questions 引用  
    questionlist.value = questionsArray;
    console.log(questionlist.value);
  };
  reader.readAsArrayBuffer(file);
  return true;
}



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
        <a-button type="primary" @click="doAdd()" style="margin-left: 400px;">新增题目</a-button>
        <a-button type="primary" @click="listAdd()" style="margin-left: 20px;">批量导入</a-button>
      </a-form-item>
    </a-form>
    <a-divider :size="0" />

    <a-table style="margin-top: 30px" :columns="columns" :data="dataList" @page-change="onPageChange" :pagination="{
      showTotal: true,
      pageSize: searchParams.pageSize,
      current: searchParams.current,
      total: parseInt(total, 10)
    }">
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag v-for="(tag, index) of record.tags" :key="index" color="blue">{{ tag }}</a-tag>
        </a-space>
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doEdit(record)">修改</a-button>
          <a-button type="primary" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
    </a-table>
    <a-modal v-model:visible="visibleModel" @ok="handleOkModel" @cancel="handleCancelModel">
      <template #title>
        批量导入
      </template>
      <a-upload action="/" :auto-upload="false" :file-list="file" :limit="1" @before-upload="handleUpload"
        accept=".xlsx, .xls" />
      <a-link href="//localhost:5173/src/assets/%E7%A4%BA%E4%BE%8B.xlsx"
        style="margin-left: 410px;margin-top: 10px;">下载示例</a-link>
    </a-modal>
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
        <a-form-item field="content" tooltip="请输入内容" label="内容">
          <MdEditer class="mdEditer" v-model="form.content" :value="form.content" :mode="'split'"
            :handle-change="onChangeContext" placeholder="please enter your post..." />
        </a-form-item>
        <a-form-item field="answer" tooltip="请输入答案" label="答案">
          <MdEditer class="mdEditer" v-model="form.answer" :value="form.answer" :mode="'split'"
            :handle-change="onChangeAnswer" placeholder="请输入" />
        </a-form-item>
        <a-form-item field="judgeConfig" label="判题配置" :content-flex="false" :merge-props="false"
          v-if="form.judgeConfig">
          <a-space direction="vertical" fill>
            <a-form-item field="judgeConfig.timeLimit" label="时间限制 ms">
              <a-input-number v-model="form.judgeConfig.timeLimit" placeholder="请输入时间限制" mode="button" size="large"
                class="input-demo" />
            </a-form-item>
            <a-form-item field="judgeConfig.memoryLimit" label="内存限制 Mb">
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
          <a-radio-group v-model="form.status">
            <a-radio :value="1">公开</a-radio>
            <a-radio :value="0">私有</a-radio>
          </a-radio-group>
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

<style>
#manageQuestion {
  min-width: 1280px;
  margin: 0 auto;
}

.arco-upload-list-item .arco-upload-progress {
  display: none;
}
</style>
