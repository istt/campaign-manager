export interface IVasCloudConfiguration {
    id?: string;
    endPoint?: string;
    username?: string;
    password?: string;
    serviceCode?: string;
    packageCode?: string;
    shortCode?: string;
    cpCode?: string;
    cpCharge?: string;
    serviceId?: string;
    rateLimit?: number;
}

export class VasCloudConfiguration implements IVasCloudConfiguration {
    constructor(
        public id?: string,
        public endPoint?: string,
        public username?: string,
        public password?: string,
        public serviceCode?: string,
        public packageCode?: string,
        public shortCode?: string,
        public cpCode?: string,
        public cpCharge?: string,
        public serviceId?: string,
        public rateLimit?: number
    ) {}
}
