import { z } from 'zod';

export const DailyStatsQuerySchema = z.object({
  date: z.string().regex(/^\d{4}-\d{2}-\d{2}$/, '日期格式必须为 YYYY-MM-DD'),
});

export const MonthlyStatsQuerySchema = z.object({
  month: z.string().regex(/^\d{4}-\d{2}$/, '月份格式必须为 YYYY-MM'),
});

export interface StatsResponse {
  labels: string[];
  datasets: {
    label: string;
    data: number[];
  }[];
}
