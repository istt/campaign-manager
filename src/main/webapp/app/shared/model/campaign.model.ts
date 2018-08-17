import { Moment } from 'moment';
import * as moment from 'moment';

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
    workingHours?: number[];
    workingWeekdays?: number[];
    spSvc?: string;
    spId?: string;
    cpId?: string;
    msgQuota?: number;
    subQuota?: number;
    rateLimit?: number;
    // Extra properties
    cfg?: any;
    stats?: any;
    datafiles?: any[];
    holidays?: Moment[];
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
        public workingHours?: number[],
        public workingWeekdays?: number[],
        public holidays?: Moment[],
        public spSvc?: string,
        public spId?: string,
        public cpId?: string,
        public msgQuota?: number,
        public subQuota?: number,
        public rateLimit?: number,
        public cfg?: any,
        public stats?: any,
        public datafiles?: any[]
    ) {
        this.cfg = {};
        this.stats = {};
        this.datafiles = [];
        this.state = 1;
        this.workingHours = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23];
        this.workingWeekdays = [0, 1, 2, 3, 4, 5, 6];
        this.startAt = moment();
        this.expiredAt = moment().add(1, 'day');
    }
}
