import type { BillCategoryCode } from './bill-category';

export interface MonthRecord {
  month: string;
  totalIncome: number;
  totalExpense: number;
  categoryStats: Record<BillCategoryCode, number>;
  lastUpdateTime: string;
  recordCount: number;
}

export function createMonthRecord(month: string): MonthRecord {
  return {
    month,
    totalIncome: 0,
    totalExpense: 0,
    categoryStats: {} as Record<BillCategoryCode, number>,
    lastUpdateTime: new Date().toISOString(),
    recordCount: 0,
  };
}
