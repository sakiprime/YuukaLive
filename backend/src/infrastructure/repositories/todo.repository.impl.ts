import { Injectable, Inject } from '@nestjs/common';
import { eq } from 'drizzle-orm';
import { BetterSQLite3Database } from 'drizzle-orm/better-sqlite3';
import { TodoRepository } from '../../domain/todo/todo.repository';
import type { Todo } from '../../domain/todo/todo.entity';
import { todos } from '../../domain/todo/todo.schema';

@Injectable()
export class TodoRepositoryImpl implements TodoRepository {
  constructor(@Inject('DB') private db: BetterSQLite3Database<any>) {}

  async findAll(): Promise<Todo[]> {
    return this.db.select().from(todos);
  }

  async findById(id: number): Promise<Todo | null> {
    const result = await this.db.select().from(todos).where(eq(todos.id, id));
    return result[0] || null;
  }

  async create(title: string, sortKey: string): Promise<Todo> {
    const result = await this.db
      .insert(todos)
      .values({ title, sortKey, urgent: false, completed: false })
      .returning();
    return result[0];
  }

  async update(id: number, todo: Partial<Todo>): Promise<Todo> {
    const result = await this.db
      .update(todos)
      .set(todo)
      .where(eq(todos.id, id))
      .returning();
    return result[0];
  }

  async delete(id: number): Promise<void> {
    await this.db.delete(todos).where(eq(todos.id, id));
  }
}
