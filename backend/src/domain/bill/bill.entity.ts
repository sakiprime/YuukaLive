import { isValidCategoryCode, getCategoryByCode } from './bill-category';
import type { BillCategoryCode } from './bill-category';

export interface Bill {
  id: number;
  amount: number;
  date: string;
  description: string | null;
  category: BillCategoryCode;
}

export type OmitBillId = Omit<Bill, 'id'>;

export function createBill(data: OmitBillId): Bill {
  if (data.amount < 0) {
    throw new Error('金额不能为负数');
  }
  if (!data.date || isNaN(Date.parse(data.date))) {
    throw new Error('日期格式无效');
  }
  if (!isValidCategoryCode(data.category)) {
    throw new Error(`无效的分类代码: ${data.category}`);
  }
  return { id: 0, ...data };
}

// 获取分类的可读标签
export function getCategoryLabel(code: BillCategoryCode): string {
  return getCategoryByCode(code)?.label ?? '未知分类';
}

// 获取分类类型（收入/支出）
export function getCategoryType(code: BillCategoryCode): 'expense' | 'income' {
  return (getCategoryByCode(code)?.type as 'expense' | 'income') ?? 'expense';
}
