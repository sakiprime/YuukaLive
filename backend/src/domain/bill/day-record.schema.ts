import { sqliteTable, text, integer } from 'drizzle-orm/sqlite-core';

export const dayRecords = sqliteTable('day_records', {
  date: text('date').primaryKey(),
  totalIncome: integer('total_income').notNull().default(0),
  totalExpense: integer('total_expense').notNull().default(0),
  categoryStats: text('category_stats').notNull().default('{}'),
  lastUpdateTime: text('last_update_time').notNull(),
  recordCount: integer('record_count').notNull().default(0),
});
