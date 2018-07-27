export interface IVasCloudConfiguration {
    id?: string;
    endPoint?: string;
    username?: string;
    password?: string;
    serviceCode?: string;
    packageCode?: string;
    cpCode?: string;
    cpCharge?: string;
    serviceId?: string;
}

export class VasCloudConfiguration implements IVasCloudConfiguration {
    constructor(
        public id?: string,
        public endPoint?: string,
        public username?: string,
        public password?: string,
        public serviceCode?: string,
        public packageCode?: string,
        public cpCode?: string,
        public cpCharge?: string,
        public serviceId?: string
    ) {}
}
