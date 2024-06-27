/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_ExamResult_ } from '../models/BaseResponse_ExamResult_';
import type { BaseResponse_ExamVO_ } from '../models/BaseResponse_ExamVO_';
import type { BaseResponse_List_ExamResultVO_ } from '../models/BaseResponse_List_ExamResultVO_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { BaseResponse_Page_ExamVO_ } from '../models/BaseResponse_Page_ExamVO_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { ExamAddRequest } from '../models/ExamAddRequest';
import type { ExamEditRequest } from '../models/ExamEditRequest';
import type { ExamQueryRequest } from '../models/ExamQueryRequest';
import type { ExamQuestionAddRequest } from '../models/ExamQuestionAddRequest';
import type { ExamQuestionEditRequest } from '../models/ExamQuestionEditRequest';
import type { ExamResultQueryRequest } from '../models/ExamResultQueryRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class ExamControllerService {
    /**
     * addExam
     * @param examAddRequest examAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addExamUsingPost(
        examAddRequest: ExamAddRequest,
    ): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/exam/add',
            body: examAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * deleteExam
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteExamUsingPost(
        deleteRequest: DeleteRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/exam/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * editExam
     * @param examEditRequest examEditRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static editExamUsingPost(
        examEditRequest: ExamEditRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/exam/edit',
            body: examEditRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * endExam
     * @param id id
     * @returns BaseResponse_boolean_ OK
     * @throws ApiError
     */
    public static endExamUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_boolean_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/exam/end',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * addExamQuestion
     * @param examQuestionAddRequest examQuestionAddRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addExamQuestionUsingPost(
        examQuestionAddRequest: ExamQuestionAddRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/exam/examQuestion/add',
            body: examQuestionAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * deleteExamQuestion
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteExamQuestionUsingPost(
        deleteRequest: DeleteRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/exam/examQuestion/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * editExamQuestion
     * @param examQuestionEditRequest examQuestionEditRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static editExamQuestionUsingPost(
        examQuestionEditRequest: ExamQuestionEditRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/exam/examQuestion/edit',
            body: examQuestionEditRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getExamResultVoByUserId
     * @param id id
     * @returns BaseResponse_ExamResult_ OK
     * @throws ApiError
     */
    public static getExamResultVoByUserIdUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_ExamResult_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/exam/examResult/get/vo',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getListExamResultVoByExamId
     * @param examResultQueryRequest examResultQueryRequest
     * @returns BaseResponse_List_ExamResultVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static getListExamResultVoByExamIdUsingPost(
        examResultQueryRequest: ExamResultQueryRequest,
    ): CancelablePromise<BaseResponse_List_ExamResultVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/exam/examResult/list/vo',
            body: examResultQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getExamVoById
     * @param id id
     * @returns BaseResponse_ExamVO_ OK
     * @throws ApiError
     */
    public static getExamVoByIdUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_ExamVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/exam/get/vo',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listMyExamVOByPage
     * @param examQueryRequest examQueryRequest
     * @returns BaseResponse_Page_ExamVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMyExamVoByPageUsingPost(
        examQueryRequest: ExamQueryRequest,
    ): CancelablePromise<BaseResponse_Page_ExamVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/exam/list/my/page',
            body: examQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listExamVOByPage
     * @param examQueryRequest examQueryRequest
     * @returns BaseResponse_Page_ExamVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listExamVoByPageUsingPost(
        examQueryRequest: ExamQueryRequest,
    ): CancelablePromise<BaseResponse_Page_ExamVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/exam/list/page',
            body: examQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
}
