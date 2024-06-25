<template>
  <div id="main" style="width: 100%; height: 400px"></div>
</template>
<script setup>
import { ref, onMounted, watch } from "vue";
//  按需引入 echarts
import * as echarts from "echarts";
let props = defineProps(["paper_id", "title", "class_id"]);
let namedata = ref();
let scoredata = ref();
onMounted(() => {
  init();
});
function init() {

      // scoredata = Array.from(Object.values(res.data.scoredata));
      // namedata = Array.from(Object.values(res.data.namedata));
      // initEchart();
}
function initEchart() {
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById("main"));
  // 指定图表的配置项和数据
  var option = {
    title: {
      text: props.title,
    },
    tooltip: {},
    legend: {
      data: [],
    },
    xAxis: {
      data: namedata,
    },
    yAxis: {},
    series: [
      {
        name: "销量",
        type: "bar",
        data: scoredata,
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
