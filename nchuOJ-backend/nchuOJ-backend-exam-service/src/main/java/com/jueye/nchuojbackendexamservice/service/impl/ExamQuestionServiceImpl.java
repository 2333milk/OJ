package com.jueye.nchuojbackendexamservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jueye.nchuojbackendcommon.common.ErrorCode;
import com.jueye.nchuojbackendcommon.exception.BusinessException;
import com.jueye.nchuojbackendexamservice.mapper.ExamQuestionMapper;
import com.jueye.nchuojbackendexamservice.service.ExamQuestionService;
import com.jueye.nchuojbackendmodel.model.entity.ExamQuestion;
import com.jueye.nchuojbackendmodel.model.entity.Question;
import com.jueye.nchuojbackendmodel.model.vo.ExamQuestionVO;
import com.jueye.nchuojbackendmodel.model.vo.QuestionVO;
import com.jueye.nchuojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author ASUS
* @description 针对表【exam_question(考试题目关联表)】的数据库操作Service实现
* @createDate 2024-06-20 21:37:40
*/
@Service
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestionMapper, ExamQuestion>
    implements ExamQuestionService {
    @Resource
    private QuestionFeignClient questionFeignClient;
    @Override
    public List<ExamQuestionVO> getQuestionVoList(List<Long> examQuestionIds) {
        List<ExamQuestion> examQuestionList = this.listByIds(examQuestionIds);
        List<ExamQuestionVO> examQuestionVOList = examQuestionList.stream().map(examQuestion -> {
            ExamQuestionVO examQuestionVO = new ExamQuestionVO();
            BeanUtil.copyProperties(examQuestion, examQuestionVO);
            Question question = questionFeignClient.getById(examQuestion.getQuestionId());
            QuestionVO questionVO = questionFeignClient.getQuestionVO(question);
            examQuestionVO.setQuestion(questionVO);
            return examQuestionVO;
        }).collect(Collectors.toList());
        return examQuestionVOList;
    }

    @Override
    public ExamQuestion getByExamIdAndQuestionId(long examId, long questionId) {
        QueryWrapper<ExamQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("examId",examId).eq("questionId",questionId);
        return this.getOne(queryWrapper);
    }


    @Override
    public void validExam(ExamQuestion examQuestion, boolean b) {
        if(examQuestion==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long examId = examQuestion.getExamId();
        Long questionId = examQuestion.getQuestionId();
        Integer score = examQuestion.getScore();
        if(examId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"考试id错误");
        }
        if(questionId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目id错误");
        }
        if(score<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"分数设置错误");
        }
    }
}




