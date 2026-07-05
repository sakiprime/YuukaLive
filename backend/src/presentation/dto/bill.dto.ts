import { z } from 'zod';
import { BILL_CATEGORIES } from '../../domain/bill/bill-category';

const categoryCodes = Object.keys(BILL_CATEGORIES) as [string, ...string[]];

export const CreateBillSchema = z.object({
  amount: z.number().int().positive(),
  date: z.string().regex(/^\d{4}-\d{2}-\d{2}$/, '日期格式必须为 YYYY-MM-DD'),
  description: z.string().nullable(),
  category: z.enum(categoryCodes),
});

export const UpdateBillSchema = CreateBillSchema.partial();

export type CreateBillDto = z.infer<typeof CreateBillSchema>;
export type UpdateBillDto = z.infer<typeof UpdateBillSchema>;
