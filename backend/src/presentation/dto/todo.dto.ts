import { z } from 'zod';

export const CreateTodoSchema = z.object({
  title: z.string().min(1, '标题不能为空'),
});

export const ReorderTodoSchema = z.object({
  prevId: z.number().nullable(),
  nextId: z.number().nullable(),
});

export type CreateTodoDto = z.infer<typeof CreateTodoSchema>;
export type ReorderTodoDto = z.infer<typeof ReorderTodoSchema>;
