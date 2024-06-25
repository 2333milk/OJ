package com.jueye.nchuojbackendexamservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jueye.nchuojbackendmodel.model.entity.ExamQuestion;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ASUS
* @description 针对表【exam_question(考试题目关联表)】的数据库操作Mapper
* @createDate 2024-06-23 19:30:12
* @Entity com.nchu.nchuoj.model.entity.ExamQuestion
*/
@Mapper
public interface ExamQuestionMapper extends BaseMapper<ExamQuestion> {

}




