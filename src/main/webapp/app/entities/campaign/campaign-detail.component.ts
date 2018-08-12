import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ICampaign } from 'app/shared/model/campaign.model';
import { JhiTrackerService } from 'app/core';

import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
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
        this.smsService
            .query(
                Object.assign(
                    {
                        page: this.page - 1,
                        size: this.itemsPerPage,
                        sort: this.sort()
                    },
                    this.searchModel,
                    { campaignId: this.campaign.id }
                )
            )
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
    saveData() {
        this.dataFileService
            .saveData()
            .subscribe(res => this.jhiAlertService.success('appApp.whitelist.save'), (err: HttpErrorResponse) => this.onError(err.message));
    }

    searchReset() {
        this.searchModel = {};
        this.transition();
    }

    search() {
        this.smsService
            .query(
                Object.assign(
                    {
                        page: this.page - 1,
                        size: this.itemsPerPage,
                        sort: this.sort()
                    },
                    this.searchModel
                )
            )
            .subscribe(
                (res: HttpResponse<ISms[]>) => this.paginateSms(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }
}
