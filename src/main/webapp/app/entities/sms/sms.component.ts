import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ISms } from 'app/shared/model/sms.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { SmsService } from './sms.service';
// Extra component
import { DataFileService } from '../data-file';
import { CampaignService } from '../campaign';
import { Observable } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, map } from 'rxjs/operators';

@Component({
    selector: 'jhi-sms',
    templateUrl: './sms.component.html'
})
export class SmsComponent implements OnInit, OnDestroy {
    currentAccount: any;
    sms: ISms[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
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
        public dataFileService: DataFileService,
        public smsService: SmsService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.smsService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
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
        this.router.navigate(['/sms'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/sms',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSms();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISms) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInSms() {
        this.eventSubscriber = this.eventManager.subscribe('smsListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'submitAt') {
            result.push('submitAt,desc');
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
    exportData() {
        this.dataFileService.exportUrl = 'api/sms/export';
        this.dataFileService.exportData('sms');
    }

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
        if (this.searchModel.campaign) {
            this.searchModel.campaignId = this.searchModel.campaign.id;
            this.searchModel.campaign = undefined;
        }
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

    searchCampaign = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(200),
            distinctUntilChanged(),
            switchMap(name => this.campaignService.query({ name }).pipe(map(v => v.body)))
        );

    campaignName = (campaign: any) => campaign.name;

    campaignId = (campaign: any) => campaign.id;
}
