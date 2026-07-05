import { Injectable, Inject } from '@nestjs/common';
import { eq } from 'drizzle-orm';
import { BetterSQLite3Database } from 'drizzle-orm/better-sqlite3';
import { MonthRecordRepository } from '../../domain/bill/month-record.repository';
import type { MonthRecord } from '../../domain/bill/month-record.entity';
import type { BillCategoryCode } from '../../domain/bill/bill-category';
import { monthRecords } from '../../domain/bill/month-record.schema';

@Injectable()
export class MonthRecordRepositoryImpl implements MonthRecordRepository {
  constructor(@Inject('DB') private db: BetterSQLite3Database<any>) {}

  async findByMonth(month: string): Promise<MonthRecord | null> {
    const result = await this.db
      .select()
      .from(monthRecords)
      .where(eq(monthRecords.month, month));
    if (!result[0]) return null;
    return this.toMonthRecord(result[0]);
  }

  async upsert(record: MonthRecord): Promise<void> {
    const existing = await this.findByMonth(record.month);
    if (existing) {
      await this.db
        .update(monthRecords)
        .set(this.toRow(record))
        .where(eq(monthRecords.month, record.month));
    } else {
      await this.db.insert(monthRecords).values(this.toRow(record));
    }
  }

  private toMonthRecord(row: any): MonthRecord {
    return {
      month: row.month,
      totalIncome: row.totalIncome,
      totalExpense: row.totalExpense,
      categoryStats: JSON.parse(row.categoryStats),
      lastUpdateTime: row.lastUpdateTime,
      recordCount: row.recordCount,
    };
  }

  private toRow(record: MonthRecord) {
    return {
      month: record.month,
      totalIncome: record.totalIncome,
      totalExpense: record.totalExpense,
      categoryStats: JSON.stringify(record.categoryStats),
      lastUpdateTime: record.lastUpdateTime,
      recordCount: record.recordCount,
    };
  }
}
