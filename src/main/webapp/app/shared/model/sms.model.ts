import { Moment } from 'moment';

export interface ISms {
    id?: string;
    source?: string;
    destination?: string;
    state?: number;
    campaignId?: string;
    cpId?: string;
    spId?: string;
    spSvc?: string;
    submitAt?: Moment;
    expiredAt?: Moment;
    submitRequestPayload?: any;
    submitResponsePayload?: any;
    deliveredAt?: Moment;
    deliveryReportPayload?: any;
}

export class Sms implements ISms {
    constructor(
        public id?: string,
        public source?: string,
        public destination?: string,
        public state?: number,
        public campaignId?: string,
        public cpId?: string,
        public spId?: string,
        public spSvc?: string,
        public submitAt?: Moment,
        public expiredAt?: Moment,
        public submitRequestPayload?: any,
        public submitResponsePayload?: any,
        public deliveredAt?: Moment,
        public deliveryReportPayload?: any
    ) {}
}
