export interface ITask {
  id?: number;
  title?: string | null;
  description?: string | null;
}

export class Task implements ITask {
  constructor(
    public id?: number,
    public title?: string | null,
    public description?: string | null,
  ) {}
}
