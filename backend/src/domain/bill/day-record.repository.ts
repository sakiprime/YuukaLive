import type { DayRecord } from './day-record.entity';

export interface DayRecordRepository {
  findByDate(date: string): Promise<DayRecord | null>;
  upsert(record: DayRecord): Promise<void>;
}
