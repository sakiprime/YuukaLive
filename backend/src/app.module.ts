import { Module } from '@nestjs/common';
import { BillModule } from './modules/bill.module';
import { TodoModule } from './modules/todo.module';

@Module({
  imports: [BillModule, TodoModule],
})
export class AppModule {}
