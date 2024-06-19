/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { JudgeCase } from './JudgeCase';
import type { JudgeConfig } from './JudgeConfig';
import type { UserVO } from './UserVO';
export type QuestionVO = {
    acceptedNum?: number;
    answer?: string;
    content?: string;
    createTime?: string;
    id?: number;
    judgeCase?: Array<JudgeCase>;
    judgeConfig?: JudgeConfig;
    submitNum?: number;
    tags?: Array<string>;
    title?: string;
    updateTime?: string;
    userId?: number;
    userVO?: UserVO;
};

