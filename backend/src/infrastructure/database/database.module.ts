import { Module, Global } from '@nestjs/common';
import Database from 'better-sqlite3';
import { drizzle } from 'drizzle-orm/better-sqlite3';
import * as schema from '../../domain/bill/bill.schema';
import * as todoSchema from '../../domain/todo/todo.schema';

const DATABASE_PATH = 'yuukalive.db';

const databaseProvider = {
  provide: 'DB',
  useFactory: () => {
    const sqlite = new Database(DATABASE_PATH);
    return drizzle(sqlite, { schema: { ...schema, ...todoSchema } });
  },
};

@Global()
@Module({
  providers: [databaseProvider],
  exports: ['DB'],
})
export class DatabaseModule {}
