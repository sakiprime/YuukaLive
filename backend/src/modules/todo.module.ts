import { Module } from '@nestjs/common';
import { TodoController } from '../presentation/todo/todo.controller';
import { TodoService } from '../application/todo/todo.service';
import { TodoRepositoryImpl } from '../infrastructure/repositories/todo.repository.impl';
import { DatabaseModule } from '../infrastructure/database/database.module';

@Module({
  imports: [DatabaseModule],
  controllers: [TodoController],
  providers: [
    TodoService,
    {
      provide: 'TodoRepository',
      useClass: TodoRepositoryImpl,
    },
  ],
})
export class TodoModule {}
