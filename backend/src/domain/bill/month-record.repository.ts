import type { MonthRecord } from './month-record.entity';

export interface MonthRecordRepository {
  findByMonth(month: string): Promise<MonthRecord | null>;
  upsert(record: MonthRecord): Promise<void>;
}
