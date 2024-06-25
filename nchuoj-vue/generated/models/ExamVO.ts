/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ExamQuestionVO } from './ExamQuestionVO';
import type { UserVO } from './UserVO';
export type ExamVO = {
    description?: string;
    endTime?: string;
    examQuestionIds?: Array<number>;
    examQuestions?: Array<ExamQuestionVO>;
    id?: number;
    startTime?: string;
    status?: number;
    title?: string;
    user?: UserVO;
};

