<script setup lang="ts">
import { ExamControllerService, type ExamVO } from '@/api';
import { Message } from '@arco-design/web-vue';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import moment from 'moment';
import { time } from 'console';
import { useExamStore } from '@/stores/exam';
const exam = ref<ExamVO>();
interface Props {
    id: string;
}
const props = withDefaults(defineProps<Props>(), {
    id: () => ''
});
const now = new Date();
const count = ref();
const router = useRouter();
const examStore = useExamStore();
const countDown = () => {
    count.value -= 1;
    if(count.value==0){
        examStore.startExam(exam.value);
    }
}

const loadData = async () => {
    const res = await ExamControllerService.getExamVoByIdUsingGet(props.id as any);
    if (res.code == 0) {
        exam.value = res.data;
        if (exam.value?.endTime != undefined) {
            count.value =Math.floor( (new Date(exam.value?.endTime).getTime() - now.getTime()-28800000) / 1000);
            console.log(count.value)
            setInterval(countDown, 1000);
        }
    } else {
        Message.error("数据加载错误，" + res.message);
    }
}
onMounted(() => {
    loadData();
});


/**
 * 跳转做题页面
 */
const goQuestionSolve = (id: any) => {
    router.push({
        path: `/submit/question/${id}`,
    })
}

</script>

<template>
    <div id="doExam">
        <div class="tap">
            <a-row class="grid-demo" style="margin-bottom: 16px;">
                <a-col flex="100px">
                    <img src="../../access/cmp.png">
                </a-col>
                <a-col flex="auto">
                    <a-typography-title :heading="2" style="margin-left: 30px;">
                        {{ exam?.title }}
                    </a-typography-title>
                </a-col>
            </a-row>

        </div>
        <div class="content">
            <a-row class="grid-demo">
                <a-col :span="12">
                    <a-card :style="{ width: '90%' }" title="题目集信息">
                        <a-typography-paragraph>
                            考试详情：
                        </a-typography-paragraph>
                        <a-typography-paragraph>
                            {{ exam?.description }}
                        </a-typography-paragraph>
                        <a-typography-paragraph>
                            开始时间:&emsp;{{ moment(exam?.startTime).utcOffset(0).format('YYYY-MM-DD HH:mm:ss') }}
                        </a-typography-paragraph>
                        <a-typography-paragraph>
                            结束时间:&emsp;{{ moment(exam?.endTime).utcOffset(0).format('YYYY-MM-DD HH:mm:ss') }}
                        </a-typography-paragraph>
                    </a-card>
                </a-col>
                <a-col :span="12">
                    <a-card :style="{ width: '90%' }" title="答题状态">
                        <a-typography-paragraph>
                            考试状态：&emsp;{{ count>0?"进行中":"已结束"}}
                        </a-typography-paragraph>

                        <div class="timer">
                            <div style="text-align: center;">距离答题结束还有</div>
                            <div style="text-align: center;margin-top: 10px;">
                                <icon-schedule />
                                {{ Math.floor(count / 3600) }}:{{ Math.floor(count % 3600 / 60) }}:{{ count % 60 }}
                            </div>
                        </div>
                    </a-card>
                </a-col>
            </a-row>


        </div>
        <div class="questions">
            <a-card :style="{ width: '95%' }" title="我的答题卡">
                <a-button class="question" v-for="(examQuestion, index) in exam?.examQuestions"
                    @click="goQuestionSolve(examQuestion.question?.id)">
                    {{ index + 1 }}
                </a-button>
            </a-card>

        </div>
    </div>
</template>

<style scoped>
.tap {
    margin-top: 20px;
}

.content {
    margin-top: 20px;
}

.questions {
    margin-top: 30px;
}

.question {
    margin-left: 10px;
}
.timer{
    margin: 40px;
}
</style>
