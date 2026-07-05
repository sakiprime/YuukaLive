import { Injectable, Inject } from '@nestjs/common';
import { eq } from 'drizzle-orm';
import { BetterSQLite3Database } from 'drizzle-orm/better-sqlite3';
import { DayRecordRepository } from '../../domain/bill/day-record.repository';
import type { DayRecord } from '../../domain/bill/day-record.entity';
import type { BillCategoryCode } from '../../domain/bill/bill-category';
import { dayRecords } from '../../domain/bill/day-record.schema';

@Injectable()
export class DayRecordRepositoryImpl implements DayRecordRepository {
  constructor(@Inject('DB') private db: BetterSQLite3Database<any>) {}

  async findByDate(date: string): Promise<DayRecord | null> {
    const result = await this.db
      .select()
      .from(dayRecords)
      .where(eq(dayRecords.date, date));
    if (!result[0]) return null;
    return this.toDayRecord(result[0]);
  }

  async upsert(record: DayRecord): Promise<void> {
    const existing = await this.findByDate(record.date);
    if (existing) {
      await this.db
        .update(dayRecords)
        .set(this.toRow(record))
        .where(eq(dayRecords.date, record.date));
    } else {
      await this.db.insert(dayRecords).values(this.toRow(record));
    }
  }

  private toDayRecord(row: any): DayRecord {
    return {
      date: row.date,
      totalIncome: row.totalIncome,
      totalExpense: row.totalExpense,
      categoryStats: JSON.parse(row.categoryStats),
      lastUpdateTime: row.lastUpdateTime,
      recordCount: row.recordCount,
    };
  }

  private toRow(record: DayRecord) {
    return {
      date: record.date,
      totalIncome: record.totalIncome,
      totalExpense: record.totalExpense,
      categoryStats: JSON.stringify(record.categoryStats),
      lastUpdateTime: record.lastUpdateTime,
      recordCount: record.recordCount,
    };
  }
}
