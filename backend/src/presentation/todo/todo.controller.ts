import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Body,
  Param,
  ParseIntPipe,
} from '@nestjs/common';
import { TodoService } from '../../application/todo/todo.service';
import { CreateTodoSchema, ReorderTodoSchema } from '../dto/todo.dto';
import type { Todo } from '../../domain/todo/todo.entity';
import { Result } from '../result';

@Controller('todos')
export class TodoController {
  constructor(private readonly todoService: TodoService) {}

  @Get()
  async findAll(): Promise<Result<Todo[]>> {
    const data = await this.todoService.findAll();
    return Result.success(data);
  }

  @Post()
  async create(@Body() body: unknown): Promise<Result<Todo>> {
    const { title } = CreateTodoSchema.parse(body);
    const data = await this.todoService.create(title);
    return Result.success(data);
  }

  @Put(':id/toggle-urgent')
  async toggleUrgent(@Param('id', ParseIntPipe) id: number): Promise<Result<Todo>> {
    const data = await this.todoService.toggleUrgent(id);
    return Result.success(data);
  }

  @Put(':id/toggle-completed')
  async toggleCompleted(@Param('id', ParseIntPipe) id: number): Promise<Result<Todo>> {
    const data = await this.todoService.toggleCompleted(id);
    return Result.success(data);
  }

  @Put(':id/reorder')
  async reorder(
    @Param('id', ParseIntPipe) id: number,
    @Body() body: unknown,
  ): Promise<Result<Todo>> {
    const { prevId, nextId } = ReorderTodoSchema.parse(body);
    const data = await this.todoService.reorder(id, prevId, nextId);
    return Result.success(data);
  }

  @Delete(':id')
  async delete(@Param('id', ParseIntPipe) id: number): Promise<Result<null>> {
    await this.todoService.delete(id);
    return Result.success(null);
  }
}
