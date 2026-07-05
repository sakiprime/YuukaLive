import { generateKeyBetween } from 'fractional-indexing';

export interface Todo {
  id: number;
  title: string;
  sortKey: string;
  urgent: boolean;
  completed: boolean;
}

export function createTodo(data: { title: string; sortKey?: string }): Todo {
  return {
    id: 0,
    title: data.title,
    sortKey: data.sortKey || generateKeyBetween(null, null),
    urgent: false,
    completed: false,
  };
}
