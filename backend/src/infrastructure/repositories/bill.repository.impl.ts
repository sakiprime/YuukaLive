import { Injectable, Inject } from '@nestjs/common';
import { eq } from 'drizzle-orm';
import { BetterSQLite3Database } from 'drizzle-orm/better-sqlite3';
import { BillRepository } from '../../domain/bill/bill.repository';
import type { Bill } from '../../domain/bill/bill.entity';
import type { BillCategoryCode } from '../../domain/bill/bill-category';
import { bills } from '../../domain/bill/bill.schema';

type BillRow = {
  id: number;
  amount: number;
  date: string;
  description: string | null;
  category: string;
};

function toBill(row: BillRow): Bill {
  return {
    ...row,
    category: row.category as BillCategoryCode,
  };
}

@Injectable()
export class BillRepositoryImpl implements BillRepository {
  constructor(@Inject('DB') private db: BetterSQLite3Database<any>) {}

  async findAll(): Promise<Bill[]> {
    const rows = await this.db.select().from(bills);
    return rows.map(toBill);
  }

  async findById(id: number): Promise<Bill | null> {
    const result = await this.db.select().from(bills).where(eq(bills.id, id));
    return result[0] ? toBill(result[0]) : null;
  }

  async create(bill: Omit<Bill, 'id'>): Promise<Bill> {
    const result = await this.db.insert(bills).values(bill).returning();
    return toBill(result[0]);
  }

  async update(id: number, bill: Partial<Bill>): Promise<Bill> {
    const result = await this.db
      .update(bills)
      .set(bill)
      .where(eq(bills.id, id))
      .returning();
    return toBill(result[0]);
  }

  async delete(id: number): Promise<void> {
    await this.db.delete(bills).where(eq(bills.id, id));
  }
}
