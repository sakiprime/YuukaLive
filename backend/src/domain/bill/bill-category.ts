// 账单分类：前端传 code，后端存 code，返回时带 label
export const BILL_CATEGORIES = {
  A: { label: '早餐', type: 'expense' },
  B: { label: '午餐', type: 'expense' },
  C: { label: '晚餐', type: 'expense' },
  D: { label: '零食', type: 'expense' },
  E: { label: '娱乐', type: 'expense' },
  F: { label: '交通', type: 'expense' },
  G: { label: '其他支出', type: 'expense' },
  H: { label: '工资', type: 'income' },
  I: { label: '其他收入', type: 'income' },
} as const;

export type BillCategoryCode = keyof typeof BILL_CATEGORIES;
export type BillCategoryType =
  (typeof BILL_CATEGORIES)[BillCategoryCode]['type'];

// 工具函数
export function getCategoryByCode(code: BillCategoryCode) {
  return BILL_CATEGORIES[code];
}

export function isValidCategoryCode(code: string): code is BillCategoryCode {
  return code in BILL_CATEGORIES;
}
