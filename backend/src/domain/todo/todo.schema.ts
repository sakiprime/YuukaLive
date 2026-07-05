import { sqliteTable, text, integer } from 'drizzle-orm/sqlite-core';

export const todos = sqliteTable('todos', {
  id: integer('id').primaryKey({ autoIncrement: true }),
  title: text('title').notNull(),
  sortKey: text('sort_key').notNull(),
  urgent: integer('urgent', { mode: 'boolean' }).notNull().default(false),
  completed: integer('completed', { mode: 'boolean' }).notNull().default(false),
});
