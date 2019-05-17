export interface ICategory {
    id?: number;
    typeName?: string;
}

export class Category implements ICategory {
    constructor(public id?: number, public typeName?: string) {}
}
