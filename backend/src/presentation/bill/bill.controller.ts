import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Body,
  Param,
  Query,
  ParseIntPipe,
} from '@nestjs/common';
import { BillService } from '../../application/bill/bill.service';
import { CreateBillSchema, UpdateBillSchema } from '../dto/bill.dto';
import {
  DailyStatsQuerySchema,
  MonthlyStatsQuerySchema,
} from '../dto/stats.dto';
import type { Bill } from '../../domain/bill/bill.entity';
import type { StatsResponse } from '../dto/stats.dto';
import { Result } from '../result';

@Controller('bills')
export class BillController {
  constructor(private readonly billService: BillService) {}

  @Get()
  async findAll(): Promise<Result<Bill[]>> {
    const data = await this.billService.findAll();
    return Result.success(data);
  }

  @Get('stats/daily')
  async getDailyStats(@Query() query: unknown): Promise<Result<StatsResponse>> {
    const { date } = DailyStatsQuerySchema.parse(query);
    const data = await this.billService.getDailyStats(date);
    return Result.success(data);
  }

  @Get('stats/monthly')
  async getMonthlyStats(@Query() query: unknown): Promise<Result<StatsResponse>> {
    const { month } = MonthlyStatsQuerySchema.parse(query);
    const data = await this.billService.getMonthlyStats(month);
    return Result.success(data);
  }

  @Get(':id')
  async findById(@Param('id', ParseIntPipe) id: number): Promise<Result<Bill | null>> {
    const data = await this.billService.findById(id);
    return Result.success(data);
  }

  @Post()
  async create(@Body() body: unknown): Promise<Result<Bill>> {
    const validated = CreateBillSchema.parse(body);
    const data = await this.billService.create(validated);
    return Result.success(data);
  }

  @Put(':id')
  async update(
    @Param('id', ParseIntPipe) id: number,
    @Body() body: unknown,
  ): Promise<Result<Bill>> {
    const validated = UpdateBillSchema.parse(body);
    const data = await this.billService.update(id, validated);
    return Result.success(data);
  }

  @Delete(':id')
  async delete(@Param('id', ParseIntPipe) id: number): Promise<Result<null>> {
    await this.billService.delete(id);
    return Result.success(null);
  }
}
