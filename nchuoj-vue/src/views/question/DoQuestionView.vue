<script setup lang="ts">
import { Message } from '@arco-design/web-vue';
import { QuestionControllerService,  type QuestionSubmitAddRequest, type QuestionVO } from '@/api';
import { onMounted, ref, withDefaults, defineProps } from 'vue';
import CodeEditer from '@/components/CodeEditer.vue';
import MdViewer from '@/components/MdViewer.vue';


const question = ref<QuestionVO>();

interface Props {
    id: string;
}
const props = withDefaults(defineProps<Props>(), {
    id: () => ''
});

const loadData = async () => {
    const res = await QuestionControllerService.getQuestionVoByIdUsingGet(props.id as any);
    if (res.code == 0) {
        question.value = res.data;
    } else {
        Message.error("数据加载错误，" + res.message);
    }
}
onMounted(() => {
    loadData();
});

const form = ref<QuestionSubmitAddRequest>({
    language: "java",
    code: "public class Main{\n" +
        "    public static void main(String[] args){\n" +
        "        int a = Integer.parseInt(args[0]);\n" +
        "        int b = Integer.parseInt(args[1]);\n" +
        "        System.out.println((a+b));\n" +
        "    }\n" +
        "}",
    questionId:props.id as any,
});

const doSubmit = async () => {
    const res = await  QuestionControllerService.doSubmitUsingPost(form.value);
    if (res.code == 0) {
        Message.success("提交成功");
    } else {
        Message.error("提交失败，" + res.message);
    }
}
</script>

<template>
    <div id="doQuestion">
        <a-row class="grid-demo">
            <a-col :span="12">
                <a-tabs default-active-key="1">
                    <a-tab-pane key="1" title="题目浏览">
                        <a-card v-if="question" :title="question.title">
                            <a-descriptions title="判题条件" :column="{ xs: 1, md: 2, lg: 3 }">
                                <a-descriptions-item label="时间限制">
                                    {{ question.judgeConfig?.timeLimit ?? 0 }}ms
                                </a-descriptions-item>
                                <a-descriptions-item label="空间限制">
                                    {{ question.judgeConfig?.memoryLimit ?? 0 }}kb
                                </a-descriptions-item>
                                <a-descriptions-item label="堆栈限制">
                                    {{ question.judgeConfig?.stackLimit ?? 0 }}
                                </a-descriptions-item>
                            </a-descriptions>
                            <MdViewer :value="question.content" />
                            <template #extra>
                                <a-space>
                                    <a-tag v-for="(tag, index) of question.tags" :key="index" color="blue" >{{
                                        tag }}</a-tag>
                                </a-space>
                            </template>
                        </a-card>
                    </a-tab-pane>
                    <a-tab-pane key="2" title="评论">
                        待完善
                    </a-tab-pane>
                    <a-tab-pane key="3" title="题解">
                        待完善
                    </a-tab-pane>
                </a-tabs>
            </a-col>
            <a-col :span="12">
                <a-form :model="form" layout="inline">
                    <a-form-item field="title" labal="编程语言">
                        <a-select :style="{ width: '100px' }" v-model="form.language" placeholder="请选择编程语言">
                            <a-option>java</a-option>
<!--                            <a-option>c++</a-option>-->
<!--                            <a-option>go</a-option>-->
                        </a-select>
                    </a-form-item>
                </a-form>
                <CodeEditer style="margin-left: 16px" :language="form.language" v-model="form.code"/>
                <a-divider :size="0" />
                <a-button type="primary" style="min-width: 200px;margin-left: 14px" @click="doSubmit()">提交代码</a-button>
            </a-col>
        </a-row>
    </div>
</template>

<style scoped></style>
