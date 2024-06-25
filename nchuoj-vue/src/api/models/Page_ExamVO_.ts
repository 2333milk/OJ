/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ExamVO } from './ExamVO';
import type { OrderItem } from './OrderItem';
export type Page_ExamVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<ExamVO>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};

