import type { Bill } from './bill.entity';

export interface BillRepository {
  findAll(): Promise<Bill[]>;
  findById(id: number): Promise<Bill | null>;
  create(bill: Omit<Bill, 'id'>): Promise<Bill>;
  update(id: number, bill: Partial<Bill>): Promise<Bill>;
  delete(id: number): Promise<void>;
}
