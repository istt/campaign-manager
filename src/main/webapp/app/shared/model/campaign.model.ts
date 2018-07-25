import { Moment } from 'moment';

export interface ICampaign {
    id?: string;
    name?: string;
    description?: any;
    code?: string;
    externalId?: string;
    channel?: string;
    state?: number;
    shortCode?: string;
    callbackUrl?: string;
    createdBy?: string;
    createdAt?: Moment;
    approvedBy?: string;
    approvedAt?: Moment;
    shortMsg?: string;
    msisdnListContentType?: string;
    msisdnList?: any;
    startAt?: Moment;
    expiredAt?: Moment;
    workingHours?: string;
    workingWeekdays?: string;
    workingDays?: string;
    spSvc?: string;
    spId?: string;
    cpId?: string;
    msgQuota?: number;
    subQuota?: number;
    rateLimit?: number;
}

export class Campaign implements ICampaign {
    constructor(
        public id?: string,
        public name?: string,
        public description?: any,
        public code?: string,
        public externalId?: string,
        public channel?: string,
        public state?: number,
        public shortCode?: string,
        public callbackUrl?: string,
        public createdBy?: string,
        public createdAt?: Moment,
        public approvedBy?: string,
        public approvedAt?: Moment,
        public shortMsg?: string,
        public msisdnListContentType?: string,
        public msisdnList?: any,
        public startAt?: Moment,
        public expiredAt?: Moment,
        public workingHours?: string,
        public workingWeekdays?: string,
        public workingDays?: string,
        public spSvc?: string,
        public spId?: string,
        public cpId?: string,
        public msgQuota?: number,
        public subQuota?: number,
        public rateLimit?: number
    ) {}
}
