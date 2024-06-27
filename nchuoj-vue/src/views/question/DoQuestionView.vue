<script setup lang="ts">
import { Message } from '@arco-design/web-vue';
import { QuestionControllerService, SendboxControllerService, type ExcuteCodeRequest, type QuestionSubmitAddRequest, type QuestionVO } from '@/api';
import { onMounted, ref, withDefaults, defineProps } from 'vue';
import CodeEditer from '@/components/CodeEditer.vue';
import MdViewer from '@/components/MdViewer.vue';
import { languages } from 'monaco-editor';


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
    questionId: props.id as any,
});

const doSubmit = async () => {
    const res = await QuestionControllerService.doSubmitUsingPost(form.value);
    if (res.code == 0) {
        Message.success("提交成功");
    } else {
        Message.error("提交失败，" + res.message);
    }
}
const sendboxDebug = ref({} as ExcuteCodeRequest);
const inputCase = ref();
const output = ref();
const watchLastSubmit = () => {

}
const onChangeAnswer = (v: string) => {
};
const doDebug = async () => {
    sendboxDebug.value.inputlist = [inputCase.value];
    sendboxDebug.value.language = form.value.language;
    sendboxDebug.value.code = form.value.code;
    const res = await SendboxControllerService.executeCodeUsingPost(sendboxDebug.value);
    if (res.code == 0) {
        if(res.data.status==1){
            output.value = res.data.outputlist[0];
        }else{
            output.value = res.data.message;
        }
    } else {
        Message.error("测试错误," + res.message);
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
                            <MdViewer :value="question.content" :handle-change="onChangeAnswer" :mode="'split'" />
                            <template #extra>
                                <a-space>
                                    <a-tag v-for="(tag, index) of question.tags" :key="index" color="blue">{{
                tag }}</a-tag>
                                </a-space>
                            </template>
                        </a-card>
                    </a-tab-pane>
                    <!-- <a-tab-pane key="2" title="评论">
                        待完善
                    </a-tab-pane>
                    <a-tab-pane key="3" title="题解">
                        待完善
                    </a-tab-pane> -->
                </a-tabs>
            </a-col>
            <a-col :span="12">
                <a-select style="width: 100px;margin-left: 16px;" v-model="form.language" placeholder="请选择编程语言">
                    <a-option>java</a-option>
                    <!--                            <a-option>c++</a-option>-->
                    <!--                            <a-option>go</a-option>-->
                </a-select>
                <CodeEditer style="margin-left: 16px;margin-top: 10px" :language="form.language" v-model="form.code" />
                <a-tabs default-active-key="1">
                    <a-tab-pane key="1" title="在线调试">
                        <a-typography-title :heading="6">
                            测试用例
                        </a-typography-title>
                        <a-textarea v-model="inputCase" />
                        <div v-if="output != undefined">
                            <a-typography-title :heading="6">
                                测试结果
                            </a-typography-title>
                            <a-textarea v-model="output" />
                        </div>
                        <a-button type="primary" @click="doDebug()">测试</a-button>
                    </a-tab-pane>
                </a-tabs>
            </a-col>
        </a-row>
        <!-- <a-button style="min-width: 200px;margin-left: 35%;margin-top: 30px;" @click="watchLastSubmit()">查看上次提交</a-button> -->
        <a-button type="primary" style="min-width: 200px;margin-left: 50%;margin-top: 30px;"
            @click="doSubmit()">提交本题作答</a-button>
    </div>
</template>

<style scoped></style>
