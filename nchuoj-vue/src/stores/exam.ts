import { reactive, ref} from 'vue'
import { defineStore } from 'pinia'
import {Message} from "@arco-design/web-vue";
import { start } from 'repl';
import { ExamControllerService, type ExamVO } from '@/api';

export const useExamStore = defineStore('exam', () => {
  const exam = reactive<ExamVO>({});

  function startExam(examNew:any){
    if(examNew.id!=exam.id){
        exam.id = examNew.id;
        exam.endTime = examNew.endTime;
        exam.startTime = examNew.startTime; 
        if(exam.endTime!=undefined&&exam.startTime!=undefined){
            let count = (new Date(exam.endTime).getTime()-new Date(exam.startTime).getTime());
            setTimeout(endExam,count);
        } 
    }
  }

  async function endExam(){
    const res = await ExamControllerService.endExamUsingGet(exam.id);
    if(res.code==0){
        Message.success("结束成功");
    }
  }

  
  return { exam,startExam}
},{
  persist: true,
},)
