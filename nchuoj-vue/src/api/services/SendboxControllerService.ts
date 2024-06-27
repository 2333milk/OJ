/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_ExcuteCodeResponse_ } from '../models/BaseResponse_ExcuteCodeResponse_';
import type { ExcuteCodeRequest } from '../models/ExcuteCodeRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class SendboxControllerService {
    /**
     * executeCode
     * @param excuteCodeRequest excuteCodeRequest
     * @returns BaseResponse_ExcuteCodeResponse_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static executeCodeUsingPost(
        excuteCodeRequest: ExcuteCodeRequest,
    ): CancelablePromise<BaseResponse_ExcuteCodeResponse_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/sendbox/executeCode',
            body: excuteCodeRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
}
