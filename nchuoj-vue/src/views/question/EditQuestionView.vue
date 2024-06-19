<script setup lang="ts">
import MdEditer from '@/components/MdEditer.vue';
import { Message } from '@arco-design/web-vue';
import {
  type JudgeCase,
  type JudgeConfig,
  type QuestionAddRequest,
  QuestionControllerService, type QuestionUpdateRequest,
} from '@/api';
import {onMounted, reactive, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';


const form = ref({}as QuestionUpdateRequest);
const route = useRoute();
const router= useRouter();
/**
 * 加载数据
 */
const loadData = async ()=>{
    const id = route.query.id;
    if(!id){
       return;
    }
    const res = await QuestionControllerService.getQuestionVoByIdUsingGet(id as any);
    if(res.code==0)
    {
        form.value.title= res.data?.title;
        form.value.tags = res.data?.tags;
        form.value.content = res.data?.content;
        form.value.answer = res.data?.answer;
        form.value.judgeConfig = res.data?.judgeConfig;
        form.value.judgeCase = res.data?.judgeCase;
    }else {
        Message.error("数据加载错误，"+res.message);
    }
}



const handleSubmit = async () => {
    const res = await QuestionControllerService.updateQuestionUsingPost(form.value);
    if(res.code==0){
        Message.success("修改成功");
        router.back();
    }else {
        Message.error("修改失败,"+res.message);
    }
};
const onChangeAnswer = (v: string) => {
    form.value.answer = v;
};

const onChangeContext = (v: string) => {
    form.value.content = v;
};

const handleAdd = () => {
    if (form.value.judgeCase) {
        form.value.judgeCase.push({
            input: "",
            output: "",
        })
    }

};
const handleDelete = (index: number) => {
    if (form.value.judgeCase) {
        form.value.judgeCase.splice(index, 1);
    }
};

onMounted(()=>{
    loadData();
})
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
const goBack = ()=>{
  router.back();
}

</script>

<template>
    <div id="setQuesiton">
      <a-row class="grid-demo" style="margin-bottom: 16px;">
        <a-col flex="100px">
          <h2>修改题目</h2>
        </a-col>
        <a-col flex="auto">
        </a-col>
        <a-col flex="100px">
          <a-button type="primary" @click="goBack()">返回</a-button>
        </a-col>
      </a-row>


        <a-form :model="form" :style="{ width: '600px' }" :rules="rules" @submit-success="handleSubmit">
            <a-form-item field="title" label="题目标题">
                <a-input v-model="form.title" placeholder="请输入题目标题" />
            </a-form-item>
            <a-form-item field="tags" label="题目标签">
                <a-input-tag v-model="form.tags" placeholder="输入题目标签" />
            </a-form-item>
            <a-form-item field="answer" tooltip="请输入答案" label="答案">
                <MdEditer class="mdEditer" v-model="form.answer" :value="form.answer" :mode="'split'" :handle-change="onChangeAnswer"
                    placeholder="请输入" />
            </a-form-item>
            <a-form-item field="content" tooltip="请输入内容" label="内容">
                <MdEditer class="mdEditer" v-model="form.content" :value="form.content" :mode="'split'"  :handle-change="onChangeContext"
                    placeholder="please enter your post..." />
            </a-form-item>
            <a-form-item field="judgeConfig" label="判题配置" :content-flex="false" :merge-props="false" v-if="form.judgeConfig">
                <a-space direction="vertical" fill>
                    <a-form-item field="judgeConfig.timeLimit" label="时间限制 ms">
                        <a-input v-model="form.judgeConfig.timeLimit" placeholder="请输入时间限制" mode="button"
                            size="large" class="input-demo" />
                    </a-form-item>
                    <a-form-item field="judgeConfig.memoryLimit" label="内存限制 kb">
                        <a-input v-model="form.judgeConfig.memoryLimit" placeholder="请输入内存限制" mode="button"
                            size="large" class="input-demo" />
                    </a-form-item>
                    <a-form-item field="judgeConfig.stackLimit" label="堆栈限制">
                        <a-input v-model="form.judgeConfig.stackLimit" placeholder="请输入堆栈限制" mode="button"
                            size="large" class="input-demo" />
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
                        <a-button status="danger" @click="handleDelete(index)"
                            :style="{ marginLeft: '10px' }">删除</a-button>
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
    </div>
</template>

<style scoped>
.mdEditer {
    min-width: 800px;
}
</style>