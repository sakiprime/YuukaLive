import { Injectable, Inject } from '@nestjs/common';
import { generateKeyBetween } from 'fractional-indexing';
import type { TodoRepository } from '../../domain/todo/todo.repository';
import type { Todo } from '../../domain/todo/todo.entity';

@Injectable()
export class TodoService {
  constructor(
    @Inject('TodoRepository') private todoRepository: TodoRepository,
  ) {}

  async findAll(): Promise<Todo[]> {
    const todos = await this.todoRepository.findAll();
    return todos.sort((a, b) => a.sortKey.localeCompare(b.sortKey));
  }

  async create(title: string): Promise<Todo> {
    const todos = await this.findAll();
    const lastKey = todos.length > 0 ? todos[todos.length - 1].sortKey : null;
    const sortKey = generateKeyBetween(lastKey, null);
    return this.todoRepository.create(title, sortKey);
  }

  async toggleUrgent(id: number): Promise<Todo> {
    const todo = await this.todoRepository.findById(id);
    if (!todo) throw new Error('Todo not found');
    return this.todoRepository.update(id, { urgent: !todo.urgent });
  }

  async toggleCompleted(id: number): Promise<Todo> {
    const todo = await this.todoRepository.findById(id);
    if (!todo) throw new Error('Todo not found');
    return this.todoRepository.update(id, { completed: !todo.completed });
  }

  async reorder(id: number, prevId: number | null, nextId: number | null): Promise<Todo> {
    const todos = await this.findAll();
    const prevKey = prevId ? todos.find(t => t.id === prevId)?.sortKey : null;
    const nextKey = nextId ? todos.find(t => t.id === nextId)?.sortKey : null;
    const newSortKey = generateKeyBetween(prevKey, nextKey);
    return this.todoRepository.update(id, { sortKey: newSortKey });
  }

  async delete(id: number): Promise<void> {
    return this.todoRepository.delete(id);
  }
}
