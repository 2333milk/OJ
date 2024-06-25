/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ExamResult } from './ExamResult';
import type { OrderItem } from './OrderItem';
export type Page_ExamResult_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<ExamResult>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};

