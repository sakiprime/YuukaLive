export class Result<T> {
  constructor(
    public code: number,
    public message: string,
    public data: T,
  ) {}

  static success<T>(data: T): Result<T> {
    return new Result(0, 'success', data);
  }

  static fail(code = -1, message = 'error'): Result<null> {
    return new Result(code, message, null);
  }
}
