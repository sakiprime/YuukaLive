import type { Todo } from './todo.entity';

export interface TodoRepository {
  findAll(): Promise<Todo[]>;
  findById(id: number): Promise<Todo | null>;
  create(title: string, sortKey: string): Promise<Todo>;
  update(id: number, todo: Partial<Todo>): Promise<Todo>;
  delete(id: number): Promise<void>;
}
