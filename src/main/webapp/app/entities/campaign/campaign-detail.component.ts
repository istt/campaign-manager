import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ICampaign } from 'app/shared/model/campaign.model';
import { CampaignService } from './campaign.service';
import { JhiTrackerService } from 'app/core';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { HttpErrorResponse, HttpHeaders, HttpResponse, HttpParams } from '@angular/common/http';
import { ISms } from 'app/shared/model/sms.model';
import { SmsService } from '../sms';
import { ITEMS_PER_PAGE } from 'app/shared';
// Extra component
import { DataFileService } from '../data-file';

@Component({
    selector: 'jhi-campaign-detail',
    templateUrl: './campaign-detail.component.html'
})
export class CampaignDetailComponent implements OnInit, OnDestroy {
    campaign: ICampaign;
    // Extra fields
    currentAccount: any;
    sms: ISms[];
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    searchModel: ISms = {};

    constructor(
        public campaignService: CampaignService,
        public smsService: SmsService,
        public dataFileService: DataFileService,
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        private trackerService: JhiTrackerService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ campaign }) => {
            this.campaign = campaign;
            this.trackerService.subscribe('/topic/campaign/' + campaign.id);
            this.trackerService.receive().subscribe(activity => Object.assign(campaign.stats, activity));
            this.itemsPerPage = ITEMS_PER_PAGE;
            this.page = 1;
            this.reverse = true;
            this.predicate = 'startAt';
            this.loadAllSms();
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.trackerService.unsubscribe();
    }

    loadAllSms() {
        let params = Object.assign(
            {
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            },
            this.searchModel,
            { campaignId: this.campaign.id }
        );
        if (this.searchModel.submitAt) {
            params = Object.assign(params, { submitAt: this.searchModel.submitAt.utc().format() });
        }
        console.log(params);
        this.smsService
            .query(params)
            .subscribe(
                (res: HttpResponse<ISms[]>) => this.paginateSms(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.loadAllSms();
    }

    clear() {
        this.page = 0;
        this.loadAllSms();
    }

    trackId(index: number, item: ISms) {
        return item.id;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'startAt') {
            result.push('startAt,desc');
        }
        return result;
    }

    private paginateSms(data: ISms[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.sms = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    // Extra functions
    searchReset() {
        this.searchModel = {};
        this.transition();
    }

    search() {
        this.loadAllSms();
    }

    changeState(state) {
        this.campaignService
            .changeState(this.campaign.id, state)
            .subscribe(
                (res: HttpResponse<ICampaign>) => Object.assign(this.campaign, res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    round(a: number, b: number) {
        return Math.round(a * 100 / b);
    }

    exportMsisdn(state, filename) {
        this.dataFileService.exportData('msisdn' + this.campaign.code + '-' + filename + '.csv', 'api/export/sms', {
            campaignId: this.campaign.id,
            state
        });
    }
}
