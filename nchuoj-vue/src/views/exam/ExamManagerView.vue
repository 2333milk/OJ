<script setup lang="ts">
import { onMounted, reactive, ref, watchEffect } from "vue";
import { type DeleteRequest, ExamControllerService, type ExamEditRequest, QuestionControllerService, type ExamAddRequest, type ExamQueryRequest, type ExamQuestionAddRequest, type ExamQuestionVO, type ExamVO, type QuestionVO } from "../../api";
import { Message } from "@arco-design/web-vue";
import moment from 'moment';
import MdViewer from '@/components/MdViewer.vue';

const searchParams = ref<ExamQueryRequest>({
    title: '',
    pageSize: 5,
    current: 1,
});
const examList = ref([]);
const total = ref();

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


const examFormAdd = reactive<ExamAddRequest>({});
const examFormEdit = reactive<ExamEditRequest>({});
const examVO = reactive<ExamVO>({})
const examFormDelete = reactive<DeleteRequest>({});
const examQuestionFormAdd = reactive<ExamQuestionAddRequest>({});

const editExamQuestion = async (exam: ExamVO) => {
    const res = await ExamControllerService.getExamVoByIdUsingGet(exam.id);
    if (res.code == 0) {
        examVO.examQuestions = res.data?.examQuestions;
        console.log(examVO);
        examQuestionFormAdd.examId = exam.id;
        statusChange.value = 4;
        visible.value = true;
    } else {
        Message.error("加载数据错误," + res.message);
    }

}

const updateExam = (exam: ExamVO) => {
    examFormEdit.id = exam.id;
    examFormEdit.description = exam.description;
    examFormEdit.title = exam.title;
    examFormEdit.startTime = moment(exam.startTime).utcOffset(0).format('YYYY-MM-DD HH:mm:ss');
    examFormEdit.endTime = moment(exam.endTime).utcOffset(0).format('YYYY-MM-DD HH:mm:ss');
    statusChange.value = 2;
    visible.value = true;

}

const deleteExam = (exam: ExamVO) => {
    examFormDelete.id = exam.id;
    statusChange.value = 3;
    visible.value = true;
}

const addExam = () => {
    examFormAdd.description = undefined;
    examFormAdd.endTime = undefined;
    examFormAdd.title = undefined;
    examFormAdd.startTime = undefined;
    statusChange.value = 1;
    visible.value = true;
}

const visible = ref(false);
const statusChange = ref(1);

const handleOk = async () => {
    if (statusChange.value === 1) {
        const res = await ExamControllerService.addExamUsingPost(examFormAdd);
        if (res.code == 0) {
            Message.success("创建成功");
        } else {
            Message.error("创建失败," + res.message);
        }
    } else if (statusChange.value === 2) {
        const res = await ExamControllerService.editExamUsingPost(examFormEdit);
        if (res.code == 0) {
            Message.success("修改成功");
        } else {
            Message.error("修改失败," + res.message);
        }
    } else if (statusChange.value === 3) {
        const res = await ExamControllerService.deleteExamUsingPost(examFormDelete);
        if (res.code == 0) {
            Message.success("删除成功");
            loadData();
        } else {
            Message.error("删除失败，" + res.message);
        }
    } else if (statusChange.value === 4) {

    }
    visible.value = false;
    loadData();
};
const handleCancel = () => {
    visible.value = false;
}
const deleteExamQuestion = async (examQuestion: ExamQuestionVO) => {
    const res = await ExamControllerService.deleteExamQuestionUsingPost({ id: examQuestion.id });
    if (res.code == 0) {
        Message.success("删除成功");
        loadData();
        visible.value =false;
    } else {
        Message.error("删除失败，" + res.message);
    }
}
const addExamQuestion = () => {
    statusChange.value = 5;
}

const rules = {
    title: [
        {
            required: true,
            message: '未输入账号',
        },
    ],
    description: [
        {
            required: true,
            message: '未输入密码',
        },
    ],
    startTime: [
        {
            required: true,
            message: '未输入密码',
        },
    ],
    endTime: [
        {
            required: true,
            message: '未输入密码',
        },
    ],
}
const questionSearchParams = ref({
    title: '',
    tags: [],
    pageSize: 5,
    current: 1,
})
const questionList = ref([] as QuestionVO[]);

const questionColumns = [
    {
        title: '标题',
        dataIndex: 'title',
    },
    {
        title: '标签',
        dataIndex: 'tags',
    },
    {
        title: '操作',
        slotName: 'optional'
    },
];
const questionHandleSubmit = async () => {
    const res = await QuestionControllerService.listQuestionVoByPageUsingPost(searchParams.value);
    if (res.code == 0) {
        questionList.value = res.data.records as QuestionVO[];
        total.value = res.data.total;
        
    } else {
        Message.error("加载数据错误," + res.message);
    }
}
const chooseQuestion = (question: QuestionVO) => {
    examQuestionFormAdd.questionId = question.id;
}
const toQuestionManager = () => {

}

const examQuestionRules = {
    score: [
        {
            required: true,
            message: '未输入账号',
        },
    ],
}
const addExamQuestionSubmit = async () => {
    console.log(examQuestionFormAdd);
    const res = await ExamControllerService.addExamQuestionUsingPost(examQuestionFormAdd);
    if (res.code == 0) {
        Message.success("添加成功");
        loadData();
        statusChange.value = 4;
        visible.value = false;
    } else {
        Message.error("添加失败，" + res.message);
    }
}
</script>
<template>
    <div id="examManager">
        <a-form :model="searchParams" layout="inline">
            <a-form-item field="title" label="考试标题">
                <a-input v-model="searchParams.title" placeholder="请输入考试标题" style="min-width: 240px;" />
            </a-form-item>
            <a-form-item>
                <a-button type="primary" html-type="submit" @click="handleSubmit">搜索</a-button>
                <a-button type="primary" html-type="submit" style="margin-left: 900px;" @click="addExam">
                    新增考试
                </a-button>
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
                    <a-button type="primary" @click="editExamQuestion(record)">管理试题</a-button>
                    <a-button type="primary" @click="updateExam(record)">修改</a-button>
                    <a-button type="primary" @click="deleteExam(record)">删除</a-button>
                </a-space>
            </template>
        </a-table>
        <a-modal v-model:visible="visible" @ok="handleOk" @cancel="handleCancel">
            <template #title>
                <div v-if="statusChange === 1">新建考试 </div>
                <div v-else-if="statusChange === 2">修改考试 </div>
                <div v-else-if="statusChange === 3">删除考试</div>
                <div v-else> 管理考试试题</div>
            </template>
            <div v-if="statusChange === 3">
                <div style="text-align: center;">您确定要删除吗</div>
                <div style="margin-top: 50px;">
                    <a-button type="primary" style="margin-left: 150px;" @click="handleOk">确定</a-button>
                    <a-button style="margin-left: 50px;" @click="handleCancel">取消</a-button>
                </div>
            </div>
            <div v-else-if="statusChange === 4">
                <a-button type="primary" @click="addExamQuestion" style="margin-left: 20px;">
                    添加题目
                </a-button>
                <div v-for="(examQuestion, index) in examVO.examQuestions" :key="index" style="margin-top: 50px;margin-left: 20px;">
                    <a-popover position="right" :title="examQuestion.question?.title">
                        <a-button class="button" >
                            {{ index+1 }}
                        </a-button>
                        <template #content>
                            <MdViewer :value="examQuestion.question?.content"  style="min-width: 300px;"/>
                            <a-button type="primary" @click="deleteExamQuestion(examQuestion)">删除</a-button>
                        </template>
                    </a-popover>
                </div>
                
            </div>
            <div v-else-if="statusChange === 1">
                <a-form :model="examFormAdd" style="max-width: 480px;margin: auto;" :rules="rules"
                    @submit-success="handleOk">
                    <a-form-item field="title" label="考试标题">
                        <a-input v-model="examFormAdd.title" placeholder="请输入考试标题" />
                    </a-form-item>
                    <a-form-item field="description" label="考试详情">
                        <a-textarea v-model="examFormAdd.description" placeholder="请输入考试详情" allow-clear />
                    </a-form-item>
                    <a-form-item field="startTime" label="开始时间">
                        <a-date-picker v-model="examFormAdd.startTime" style="width: 220px;" placeholder="请选择开始时间"
                            show-time format="YYYY-MM-DD HH:mm:ss" />
                    </a-form-item>
                    <a-form-item field="endTime" label="结束时间">
                        <a-date-picker v-model="examFormAdd.endTime" style="width: 220px;" placeholder="请选择结束时间"
                            show-time format="YYYY-MM-DD HH:mm:ss" />
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" html-type="submit" :style="{ width: '200px' }">提交</a-button>
                    </a-form-item>
                </a-form>
            </div>
            <div v-else-if="statusChange === 2">
                <a-form :model="examFormEdit" style="max-width: 480px;margin: auto;" :rules="rules"
                    @submit-success="handleOk">
                    <a-form-item field="title" label="考试标题">
                        <a-input v-model="examFormEdit.title" placeholder="请输入考试标题" />
                    </a-form-item>
                    <a-form-item field="description" label="考试详情">
                        <a-textarea v-model="examFormEdit.description" placeholder="请输入考试详情" allow-clear />
                    </a-form-item>
                    <a-form-item field="startTime" label="开始时间">
                        <a-date-picker v-model="examFormEdit.startTime" style="width: 220px;" placeholder="请选择开始时间"
                            show-time format="YYYY-MM-DD HH:mm:ss" />
                    </a-form-item>
                    <a-form-item field="endTime" label="结束时间">
                        <a-date-picker v-model="examFormEdit.endTime" style="width: 220px;" placeholder="请选择结束时间"
                            show-time format="YYYY-MM-DD HH:mm:ss" />
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" html-type="submit" :style="{ width: '200px' }">提交</a-button>
                    </a-form-item>
                </a-form>
            </div>
            <div v-else>
                <a-form :model="questionSearchParams" layout="inline" @submit="questionHandleSubmit">
                    <a-form-item field="title" label="题目标题">
                        <a-input v-model="questionSearchParams.title" placeholder="请输入题目标题" style="min-width: 240px;" />
                    </a-form-item>
                    <a-form-item field="tags" label="题目标签">
                        <a-input-tag v-model="questionSearchParams.tags" placeholder="输入题目标签"
                            style="min-width: 240px;" />
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" html-type="submit">搜索</a-button>
                    </a-form-item>
                </a-form>
                <a-divider :size="0" />

                <a-table style="margin-top: 30px" :columns="questionColumns" :data="questionList"
                    @page-change="onPageChange" :pagination="{
            showTotal: true,
            pageSize: questionSearchParams.pageSize,
            current: questionSearchParams.current,
            total: parseInt(total, 10)
        }">
                    <template #optional="{ record }">
                        <a-space>
                            <a-button type="primary" v-if="examQuestionFormAdd.questionId === record.id"
                                @click="chooseQuestion(record)" disabled>已选择</a-button>
                            <a-button type="primary" v-else @click="chooseQuestion(record)">选择</a-button>
                        </a-space>
                    </template>
                </a-table>
                <a-row class="grid-demo">
                    <a-col :span="16">
                        <div>没有合适的题目，点击按钮跳转创建题目</div>
                    </a-col>
                    <a-col :span="8">
                        <a-button type="primary" size="mini" @click="toQuestionManager">>></a-button>
                    </a-col>
                </a-row>
                <a-form style="margin-top: 20px;" :model="examQuestionFormAdd" @submit="questionHandleSubmit"
                    :rules="examQuestionRules" @submit-success="addExamQuestionSubmit">
                    <a-form-item field="score" label="题目分数">
                        <a-input-number v-model="examQuestionFormAdd.score" placeholder="请输入题目分数" />
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" html-type="submit" style="min-width: 200px">提交</a-button>
                    </a-form-item>
                </a-form>
            </div>
            <template #footer>
                <div></div>
            </template>
        </a-modal>
    </div>
</template>
<style></style>