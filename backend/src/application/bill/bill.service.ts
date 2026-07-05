import { Injectable, Inject } from '@nestjs/common';
import type { BillRepository } from '../../domain/bill/bill.repository';
import type { Bill } from '../../domain/bill/bill.entity';
import { createBill } from '../../domain/bill/bill.entity';
import type { BillCategoryCode } from '../../domain/bill/bill-category';
import { BILL_CATEGORIES } from '../../domain/bill/bill-category';
import { getCategoryType } from '../../domain/bill/bill.entity';
import type { DayRecordRepository } from '../../domain/bill/day-record.repository';
import type { MonthRecordRepository } from '../../domain/bill/month-record.repository';
import { createDayRecord } from '../../domain/bill/day-record.entity';
import { createMonthRecord } from '../../domain/bill/month-record.entity';
import type { StatsResponse } from '../../presentation/dto/stats.dto';

@Injectable()
export class BillService {
  constructor(
    @Inject('BillRepository') private billRepository: BillRepository,
    @Inject('DayRecordRepository')
    private dayRecordRepository: DayRecordRepository,
    @Inject('MonthRecordRepository')
    private monthRecordRepository: MonthRecordRepository,
  ) {}

  async findAll(): Promise<Bill[]> {
    return this.billRepository.findAll();
  }

  async findById(id: number): Promise<Bill | null> {
    return this.billRepository.findById(id);
  }

  async create(bill: {
    amount: number;
    date: string;
    category: string;
    description: string | null;
  }): Promise<Bill> {
    const validated = createBill({
      ...bill,
      category: bill.category as BillCategoryCode,
    });
    const result = await this.billRepository.create(validated);
    await this.syncSummaryAfterCreate(result);
    return result;
  }

  async update(
    id: number,
    bill: {
      amount?: number;
      date?: string;
      category?: string;
      description?: string | null;
    },
  ): Promise<Bill> {
    const oldBill = await this.billRepository.findById(id);
    if (!oldBill) throw new Error('账单不存在');

    const updates = {
      ...bill,
      category: bill.category as BillCategoryCode | undefined,
    };
    const newBill = await this.billRepository.update(id, updates as Partial<Bill>);
    await this.syncSummaryAfterUpdate(oldBill, newBill);
    return newBill;
  }

  async delete(id: number): Promise<void> {
    const oldBill = await this.billRepository.findById(id);
    if (!oldBill) throw new Error('账单不存在');
    await this.billRepository.delete(id);
    await this.syncSummaryAfterDelete(oldBill);
  }

  async getDailyStats(date: string): Promise<StatsResponse> {
    const dayRecord = await this.dayRecordRepository.findByDate(date);
    if (!dayRecord) return { labels: [], datasets: [] };
    return this.formatStatsResponse(dayRecord.categoryStats);
  }

  async getMonthlyStats(month: string): Promise<StatsResponse> {
    const monthRecord = await this.monthRecordRepository.findByMonth(month);
    if (!monthRecord) return { labels: [], datasets: [] };
    return this.formatStatsResponse(monthRecord.categoryStats);
  }

  private formatStatsResponse(categoryStats: Record<BillCategoryCode, number>): StatsResponse {
    const labels: string[] = [];
    const expenseData: number[] = [];
    const incomeData: number[] = [];

    for (const [code, amount] of Object.entries(categoryStats)) {
      const category = BILL_CATEGORIES[code as BillCategoryCode];
      if (!category || amount === 0) continue;
      labels.push(category.label);
      if (category.type === 'expense') {
        expenseData.push(amount);
        incomeData.push(0);
      } else {
        expenseData.push(0);
        incomeData.push(amount);
      }
    }

    return {
      labels,
      datasets: [
        { label: '支出', data: expenseData },
        { label: '收入', data: incomeData },
      ],
    };
  }

  private async syncSummaryAfterCreate(bill: Bill): Promise<void> {
    await this.addToSummary(bill);
  }

  private async syncSummaryAfterUpdate(oldBill: Bill, newBill: Bill): Promise<void> {
    await this.subtractFromSummary(oldBill);
    await this.addToSummary(newBill);
  }

  private async syncSummaryAfterDelete(bill: Bill): Promise<void> {
    await this.subtractFromSummary(bill);
  }

  private async addToSummary(bill: Bill): Promise<void> {
    const date = bill.date;
    const month = date.substring(0, 7);
    const categoryType = getCategoryType(bill.category);

    let dayRecord = await this.dayRecordRepository.findByDate(date);
    if (!dayRecord) dayRecord = createDayRecord(date);
    dayRecord.recordCount += 1;
    dayRecord.lastUpdateTime = new Date().toISOString();
    dayRecord.categoryStats[bill.category] = (dayRecord.categoryStats[bill.category] || 0) + bill.amount;
    if (categoryType === 'income') dayRecord.totalIncome += bill.amount;
    else dayRecord.totalExpense += bill.amount;
    await this.dayRecordRepository.upsert(dayRecord);

    let monthRecord = await this.monthRecordRepository.findByMonth(month);
    if (!monthRecord) monthRecord = createMonthRecord(month);
    monthRecord.recordCount += 1;
    monthRecord.lastUpdateTime = new Date().toISOString();
    monthRecord.categoryStats[bill.category] = (monthRecord.categoryStats[bill.category] || 0) + bill.amount;
    if (categoryType === 'income') monthRecord.totalIncome += bill.amount;
    else monthRecord.totalExpense += bill.amount;
    await this.monthRecordRepository.upsert(monthRecord);
  }

  private async subtractFromSummary(bill: Bill): Promise<void> {
    const date = bill.date;
    const month = date.substring(0, 7);
    const categoryType = getCategoryType(bill.category);

    const dayRecord = await this.dayRecordRepository.findByDate(date);
    if (dayRecord) {
      dayRecord.recordCount -= 1;
      dayRecord.lastUpdateTime = new Date().toISOString();
      dayRecord.categoryStats[bill.category] = (dayRecord.categoryStats[bill.category] || 0) - bill.amount;
      if (categoryType === 'income') dayRecord.totalIncome -= bill.amount;
      else dayRecord.totalExpense -= bill.amount;
      await this.dayRecordRepository.upsert(dayRecord);
    }

    const monthRecord = await this.monthRecordRepository.findByMonth(month);
    if (monthRecord) {
      monthRecord.recordCount -= 1;
      monthRecord.lastUpdateTime = new Date().toISOString();
      monthRecord.categoryStats[bill.category] = (monthRecord.categoryStats[bill.category] || 0) - bill.amount;
      if (categoryType === 'income') monthRecord.totalIncome -= bill.amount;
      else monthRecord.totalExpense -= bill.amount;
      await this.monthRecordRepository.upsert(monthRecord);
    }
  }
}
