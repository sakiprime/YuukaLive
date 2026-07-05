import { sqliteTable, text, integer } from 'drizzle-orm/sqlite-core';

export const bills = sqliteTable('bills', {
  id: integer('id').primaryKey({ autoIncrement: true }),
  amount: integer('amount').notNull(),
  date: text('date').notNull(),
  description: text('description'),
  category: text('category').notNull(),
});
