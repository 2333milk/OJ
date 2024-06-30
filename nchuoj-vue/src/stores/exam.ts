import { reactive, ref} from 'vue'
import { defineStore } from 'pinia'
import {Message} from "@arco-design/web-vue";
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
            setTimeout(nearEndExam,count-5*60*1000);
            setTimeout(endExam,count);
        } 
    }
  }

  async function endExam(){
    const res = await ExamControllerService.endExamUsingGet(exam.id);
    if(res.code==0){
        Message.success("考试结束");
    }
  }

  function nearEndExam(){
    Message.warning("考试还剩5分钟!");
  }

  
  return { exam,startExam}
},{
  persist: true,
},)
