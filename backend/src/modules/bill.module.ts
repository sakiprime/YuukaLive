import { Module } from '@nestjs/common';
import { BillController } from '../presentation/bill/bill.controller';
import { BillService } from '../application/bill/bill.service';
import { BillRepositoryImpl } from '../infrastructure/repositories/bill.repository.impl';
import { DayRecordRepositoryImpl } from '../infrastructure/repositories/day-record.repository.impl';
import { MonthRecordRepositoryImpl } from '../infrastructure/repositories/month-record.repository.impl';
import { DatabaseModule } from '../infrastructure/database/database.module';

@Module({
  imports: [DatabaseModule],
  controllers: [BillController],
  providers: [
    BillService,
    {
      provide: 'BillRepository',
      useClass: BillRepositoryImpl,
    },
    {
      provide: 'DayRecordRepository',
      useClass: DayRecordRepositoryImpl,
    },
    {
      provide: 'MonthRecordRepository',
      useClass: MonthRecordRepositoryImpl,
    },
  ],
})
export class BillModule {}
