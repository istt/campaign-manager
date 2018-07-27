export interface IDataFile {
    id?: string;
    name?: string;
    tags?: string;
    dataContentType?: string;
    data?: any;
}

export class DataFile implements IDataFile {
    constructor(public id?: string, public name?: string, public tags?: string, public dataContentType?: string, public data?: any) {}
}
