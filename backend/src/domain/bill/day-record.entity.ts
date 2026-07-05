import type { BillCategoryCode } from './bill-category';

export interface DayRecord {
  date: string;
  totalIncome: number;
  totalExpense: number;
  categoryStats: Record<BillCategoryCode, number>;
  lastUpdateTime: string;
  recordCount: number;
}

export function createDayRecord(date: string): DayRecord {
  return {
    date,
    totalIncome: 0,
    totalExpense: 0,
    categoryStats: {} as Record<BillCategoryCode, number>,
    lastUpdateTime: new Date().toISOString(),
    recordCount: 0,
  };
}
