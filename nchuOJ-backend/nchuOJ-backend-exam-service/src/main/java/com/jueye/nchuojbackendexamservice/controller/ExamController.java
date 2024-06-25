package com.jueye.nchuojbackendexamservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jueye.nchuojbackendcommon.common.BaseResponse;
import com.jueye.nchuojbackendcommon.common.DeleteRequest;
import com.jueye.nchuojbackendcommon.common.ErrorCode;
import com.jueye.nchuojbackendcommon.common.ResultUtils;
import com.jueye.nchuojbackendcommon.exception.BusinessException;
import com.jueye.nchuojbackendcommon.exception.ThrowUtils;
import com.jueye.nchuojbackendexamservice.service.ExamQuestionService;
import com.jueye.nchuojbackendexamservice.service.ExamResultService;
import com.jueye.nchuojbackendexamservice.service.ExamService;
import com.jueye.nchuojbackendmodel.model.dto.exam.*;
import com.jueye.nchuojbackendmodel.model.entity.*;
import com.jueye.nchuojbackendmodel.model.vo.ExamVO;
import com.jueye.nchuojbackendserviceclient.service.QuestionFeignClient;
import com.jueye.nchuojbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
@Slf4j
public class ExamController {
    @Resource
    private ExamService examService;

    @Resource
    private ExamResultService examResultService;

    @Resource
    private ExamQuestionService examQuestionService;

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private UserFeignClient userFeignClient;

    @GetMapping("/get/vo")
    public BaseResponse<ExamVO> getExamVoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Exam exam = examService.getById(id);
        return ResultUtils.success(examService.getExamVo(exam));
    }
    /**
     * 分页获取列表（封装类）
     *
     * @param examQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ExamVO>> listExamVOByPage(@RequestBody ExamQueryRequest examQueryRequest) {
        long current = examQueryRequest.getCurrent();
        long size = examQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Exam> examPage = examService.page(new Page<>(current, size),
                examService.getQueryWrapper(examQueryRequest));
        Page<ExamVO> examVOPage = examService.getExamVOPage(examPage);
        return ResultUtils.success(examVOPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param examQueryRequest
     * @return
     */
    @PostMapping("/list/my/page")
    public BaseResponse<Page<ExamVO>> listMyExamVOByPage(@RequestBody ExamQueryRequest examQueryRequest, HttpServletRequest request)  {
        long current = examQueryRequest.getCurrent();
        long size = examQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        User loginUser = userFeignClient.getLoginUser(request);
        examQueryRequest.setUserId(loginUser.getId());
        Page<Exam> examPage = examService.page(new Page<>(current, size),
                examService.getQueryWrapper(examQueryRequest));
        Page<ExamVO> examVOPage = examService.getExamVOPage(examPage);
        return ResultUtils.success(examVOPage);
    }


    /**
     * 创建
     *
     * @param examAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addExam(@RequestBody ExamAddRequest examAddRequest, HttpServletRequest request) {
        if (examAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Exam exam = new Exam();
        BeanUtils.copyProperties(examAddRequest, exam);
        // 参数校验
        examService.validExam(exam, true);
        User loginUser = userFeignClient.getLoginUser(request);
        exam.setUserId(loginUser.getId());
        boolean result = examService.save(exam);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newExamId = exam.getId();
        return ResultUtils.success(newExamId);
    }

    /**
     * 编辑（用户）
     *
     * @param examEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editExam(@RequestBody ExamEditRequest examEditRequest, HttpServletRequest request) {
        if (examEditRequest == null || examEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Exam exam = new Exam();
        BeanUtils.copyProperties(examEditRequest, exam);
        // 参数校验
        examService.validExam(exam, false);
        User loginUser = userFeignClient.getLoginUser(request);
        long id = examEditRequest.getId();
        // 判断是否存在
        Exam oldExam = examService.getById(id);
        ThrowUtils.throwIf(oldExam == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldExam.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = examService.updateById(exam);
        return ResultUtils.success(result);
    }


    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteExam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Exam oldExam = examService.getById(id);
        ThrowUtils.throwIf(oldExam == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldExam.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = examService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 结束考试
     */
    @GetMapping("/end")
    public BaseResponse<Boolean> endExam(long id) {
        if(id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(examService.endExam(id));
    }


    /**
     * 查看成绩
     */
    @GetMapping("/examResult/get/vo")
    public BaseResponse<ExamResult> getExamResultVoByUserId(long id, HttpServletRequest request){
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        ExamResult examResult = examResultService.getByUserId(id, loginUser.getId());
        if(examResult==null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未参加考试，无成绩");
        }
        return ResultUtils.success(examResult);
    }

    /**
     * 查看总成绩
     */
    @PostMapping("/examResult/list/page/vo")
    public BaseResponse<Page<ExamResult>> getExamResultVoByExamId(@RequestBody ExamResultQueryRequest examResultQueryRequest, HttpServletRequest request){
        long current = examResultQueryRequest.getCurrent();
        long size = examResultQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ExamResult> examResultPage = examResultService.page(new Page<>(current, size),
                examResultService.getQueryWrapper(examResultQueryRequest));
        return ResultUtils.success(examResultPage);
    }

    /**
     * 添加考试试题
     */
    @PostMapping("/examQuestion/add")
    public BaseResponse<Boolean> addExamQuestion(@RequestBody ExamQuestionAddRequest examQuestionAddRequest){
        if(examQuestionAddRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ExamQuestion examQuestion = new ExamQuestion();
        BeanUtils.copyProperties(examQuestionAddRequest,examQuestion);
        long examId = examQuestionAddRequest.getExamId();
        long questionId = examQuestionAddRequest.getQuestionId();
        Exam exam = examService.getById(examId);
        Question question = questionFeignClient.getById(questionId);
        if(exam==null||question==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"考试或试题不存在");
        }
        boolean result = examQuestionService.save(examQuestion);
        ExamQuestion  examQuestionNew = examQuestionService.getByExamIdAndQuestionId(examId,questionId);
        if(result){
            examService.addExamQuestionNum(examId,examQuestionNew.getId());
        }
        return ResultUtils.success(result);
    }

    /**
     * 修改考试试题
     */
    @PostMapping("/examQuestion/edit")
    public BaseResponse<Boolean> editExamQuestion(@RequestBody ExamQuestionEditRequest examQuestionEditRequest,HttpServletRequest request){
        if (examQuestionEditRequest == null || examQuestionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ExamQuestion examQuestion = new ExamQuestion();
        BeanUtils.copyProperties(examQuestionEditRequest,examQuestion);
        // 参数校验
        examQuestionService.validExam(examQuestion, false);
        User loginUser = userFeignClient.getLoginUser(request);
        long id = examQuestionEditRequest.getId();
        // 判断是否存在
        ExamQuestion oldExamQuestion = examQuestionService.getById(id);
        ThrowUtils.throwIf(oldExamQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = examQuestionService.updateById(examQuestion);
        return ResultUtils.success(result);
    }



    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/examQuestion/delete")
    public BaseResponse<Boolean> deleteExamQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        ExamQuestion oldExamQuestion = examQuestionService.getById(id);
        ThrowUtils.throwIf(oldExamQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅管理员可删除
        if (!userFeignClient.isAdmin(userFeignClient.getLoginUser(request))) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = examQuestionService.removeById(id);
        if(b){
            examService.removeExamQuestion(oldExamQuestion.getExamId(),id);
        }
        return ResultUtils.success(b);
    }

}
