<template>
  <div id="main" style="width: 100%; height: 400px"></div>
</template>
<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
//  按需引入 echarts
import * as echarts from "echarts";
import { ExamControllerService, type ExamResultVO } from "@/api";
import { Message } from "@arco-design/web-vue";
import { reactive } from "vue";
let props = defineProps(["exam_id", "exam_title"]);
let namedata = ref([] as string[]);
let scoredata = ref([] as number[]);
const examResultVO = reactive<ExamResultVO[]>([])

watch(props.exam_id,() => {
  init();
});

onMounted(() => {
  init();
})

async function init() {
  namedata.value = [] as string[];
  scoredata.value = [] as number[];
  const res = await ExamControllerService.getListExamResultVoByExamIdUsingPost({ examId: props.exam_id });
  if (res.code == 0) {
    examResultVO.push(...res.data);
    examResultVO.forEach((examResult,dataIndex)=>{
      if(examResult.user?.userName!=undefined){
        namedata.value.push(examResult.user?.userName);
      }
      if(examResult.score!=undefined){
        scoredata.value.push(examResult.score);
      }
    })
    initEchart();
  } else {
    Message.error("加载数据错误," + res.message);
  }
}
function initEchart() {
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById("main"));
  // 指定图表的配置项和数据
  var option = {
    title: {
      text: props.exam_title,
    },
    tooltip: {},
    legend: {
      data: [],
    },
    xAxis: {
      data: namedata.value,
    },
    yAxis: {},
    series: [
      {
        name: "分数",
        type: "bar",
        data: scoredata.value,
      },
    ],
  };
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
  window.addEventListener("resize", function () {
    myChart.resize();
  });
}
watch(props, () => {
  init();
});
</script>

<style scoped></style>
