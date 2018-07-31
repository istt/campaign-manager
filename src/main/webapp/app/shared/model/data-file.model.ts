import { Moment } from 'moment';

export interface IDataFile {
    id?: string;
    name?: string;
    tags?: string;
    dataContentType?: string;
    data?: any;
    dataCsvHeaders?: string[];
    dataCsvPreview?: string;
    uploadAt?: Moment;
    processAt?: Moment;
}

export class DataFile implements IDataFile {
    constructor(
        public id?: string,
        public name?: string,
        public tags?: string,
        public dataContentType?: string,
        public data?: any,
        public dataCsvHeaders?: string[],
        public dataCsvPreview?: string,
        public uploadAt?: Moment,
        public processAt?: Moment
    ) {
        this.dataCsvHeaders = [];
    }
}
