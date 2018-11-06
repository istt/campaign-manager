import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiEventManager, JhiAlertService, JhiParseLinks } from 'ng-jhipster';

import { ICampaign } from 'app/shared/model/campaign.model';
import { CampaignService } from './campaign.service';
// Extra services
import { IVasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';
import { VasCloudConfigurationService } from '../vas-cloud-configuration';
import { ITEMS_PER_PAGE } from 'app/shared';
import { IDataFile, DataFile } from 'app/shared/model/data-file.model';
import { DataFileService } from '../data-file';
import { NgbModal, NgbModalRef, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-campaign-update',
    templateUrl: './campaign-update.component.html'
})
export class CampaignUpdateComponent implements OnInit {
    private _campaign: ICampaign;
    isSaving: boolean;
    createdAt: string;
    approvedAt: string;
    startAt: string;
    expiredAt: string;
    // Available working hours and weekdays
    workingHours = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23];
    workingWeekdays = [0, 1, 2, 3, 4, 5, 6];
    // Data File Service browsing
    eventSubscriber: Subscription;
    routeData: any;
    modalRef: NgbModalRef;

    constructor(
        public campaignService: CampaignService,
        public vasCloudSvc: VasCloudConfigurationService,
        public dataFileService: DataFileService,
        private parseLinks: JhiParseLinks,
        private eventManager: JhiEventManager,
        private jhiAlertService: JhiAlertService,
        private modalService: NgbModal,
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.dataFileService.itemsPerPage = ITEMS_PER_PAGE;
        this.dataFileService.checked = {};
        this.vasCloudSvc.selected = undefined;
        this.activatedRoute.data.subscribe(({ campaign }) => {
            this.campaign = campaign;
            this.campaign.datafiles = [];
            if (campaign.cfg && campaign.cfg['VASCLOUD']) {
                this.vasCloudSvc.selected = campaign.cfg['VASCLOUD'].id;
                this.setVasCloudCfg(campaign.cfg['VASCLOUD'].id);
            }
            this.vasCloudSvc.query().subscribe(res => (this.vasCloudSvc.entities = res.body));
            // this.dataFileService.entity = new DataFile();
            // this.dataFileService.query().subscribe(res => {
            //   this.dataFileService.entities = res.body;
            //   res.body.forEach(v => {
            //     this.dataFileService.checked[v.id] = v;
            //   });
            //   // this.campaign.datafiles
            //   // .forEach(e => this.dataFileService.checked[e.id] = Object.assign(e, { checked: true }));
            // });
            // this.loadDataFilePage(1);
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

    // Save the campaign
    save() {
        this.isSaving = true;
        this.campaign.createdAt = moment(this.createdAt, DATE_TIME_FORMAT);
        this.campaign.approvedAt = moment(this.approvedAt, DATE_TIME_FORMAT);
        // this.campaign.startAt = moment(this.startAt, DATE_TIME_FORMAT);
        const startAtTime = this.startAt.split(':');
        this.campaign.startAt = this.campaign.startAt
            .local()
            .set('hour', parseInt(startAtTime[0], 10))
            .set('minute', parseInt(startAtTime[1], 10))
            .set('second', parseInt(startAtTime[2], 10));
        const expiredAtTime = this.expiredAt.split(':');
        this.campaign.expiredAt = this.campaign.expiredAt
            .local()
            .set('hour', parseInt(expiredAtTime[0], 10))
            .set('minute', parseInt(expiredAtTime[1], 10))
            .set('second', parseInt(expiredAtTime[2], 10));
        this.campaign.code = this.campaign.name; /// OVERRIDE
        this.campaign.datafiles = Object.getOwnPropertyNames(this.dataFileService.checked)
            .map(id => (this.dataFileService.checked[id].checked ? this.dataFileService.checked[id] : false))
            .filter(v => v);
        if (this.campaign.id !== undefined) {
            this.subscribeToSaveResponse(this.campaignService.update(this.campaign));
        } else {
            this.subscribeToSaveResponse(this.campaignService.create(this.campaign));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICampaign>>) {
        result.subscribe((res: HttpResponse<ICampaign>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get campaign() {
        return this._campaign;
    }

    set campaign(campaign: ICampaign) {
        this._campaign = campaign;
        this.createdAt = moment(campaign.createdAt).format(DATE_TIME_FORMAT);
        this.approvedAt = moment(campaign.approvedAt).format(DATE_TIME_FORMAT);
        this.startAt = campaign.startAt.local().format('HH:mm:ss');
        this.expiredAt = campaign.expiredAt.local().format('HH:mm:ss');
    }
    // Vas Cloud configuration handle
    setVasCloudCfg(cfgId) {
        if (this.vasCloudSvc.entities) {
            this.vasCloudSvc.entity = this.vasCloudSvc.entities.filter(v => v.id === cfgId)[0];
            this.campaign.cfg['VASCLOUD'] = this.vasCloudSvc.entity;
            this.campaign.cpId = this.vasCloudSvc.entity.cpCode;
            this.campaign.spId = this.vasCloudSvc.entity.serviceId;
            this.campaign.shortCode = this.vasCloudSvc.entity.shortCode;
            this.campaign.channel = 'VASCLOUD/SMSGW/' + this.vasCloudSvc.entity.serviceCode + '/' + this.vasCloudSvc.entity.packageCode;
        }
    }
    // Data File handle
    loadAll() {
        return this.dataFileService
            .query({
                page: this.dataFileService.page - 1,
                size: this.dataFileService.itemsPerPage,
                sort: this.sort()
            })
            .toPromise()
            .then(
                (res: HttpResponse<IDataFile[]>) => this.paginateDataFiles(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadDataFilePage(page: number) {
        if (page !== this.dataFileService.previousPage) {
            this.dataFileService.previousPage = page;
            this.dataFileService.page = page;
            this.transition();
        }
    }

    transition() {
        this.loadAll();
    }

    clear() {
        this.dataFileService.page = 0;
        this.loadAll();
    }

    registerChangeInDataFiles() {
        this.eventSubscriber = this.eventManager.subscribe('dataFileListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.dataFileService.predicate + ',' + (this.dataFileService.reverse ? 'asc' : 'desc')];
        if (this.dataFileService.predicate !== 'uploadAt') {
            result.push('uploadAt,desc');
        }
        return result;
    }

    private paginateDataFiles(data: IDataFile[], headers: HttpHeaders) {
        this.dataFileService.links = this.parseLinks.parse(headers.get('link'));
        this.dataFileService.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.dataFileService.queryCount = this.dataFileService.totalItems;
        this.dataFileService.entities = data;
        data.forEach(v => {
            this.dataFileService.checked[v.id] = Object.assign(
                this.dataFileService.checked[v.id] ? this.dataFileService.checked[v.id] : {},
                v
            );
        });
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
    // Data File Modal handle
    openModal(modalContent) {
        this.dataFileService.entity = new DataFile();
        this.modalRef = this.modalService.open(modalContent);
        this.modalRef.result.then(
            result => {
                console.log(`Closed with: ${result}`);
            },
            reason => {
                console.error(`Dismissed ${this.getDismissReason(reason)}`);
            }
        );
    }

    saveDataFile() {
        console.log('Prepare to save data file...');
        this.isSaving = true;
        this.dataFileService.create(this.dataFileService.entity).subscribe(
            res => {
                console.log('Successfully submit data file');
                this.dataFileService.entity = res.body;
                this.isSaving = false;
                this.loadAll().then(
                    resolve =>
                        (this.dataFileService.checked[this.dataFileService.entity.id] = Object.assign(this.dataFileService.entity, {
                            checked: true
                        })),
                    reject => console.error(reject)
                );
                this.modalRef.close();
            },
            err => this.onError(err)
        );
    }
    private getDismissReason(reason: any): string {
        if (reason === ModalDismissReasons.ESC) {
            return 'by pressing ESC';
        } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
            return 'by clicking on a backdrop';
        } else {
            return `with: ${reason}`;
        }
    }
    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
        // Try to extract headers from current open file
        if (event && event.target.files && event.target.files[0]) {
            const file_1 = event.target.files[0];
            this.dataFileService.entity.name = file_1.name;
            if (file_1.type.match(/text/i)) {
                const fileReader = new FileReader();
                fileReader.onload = e => {
                    this.dataFileService.entity.dataCsvPreview = fileReader.result.toString(); // first line
                    // console.log('First line', this.dataFile.dataCsvPreview);
                    this.dataFileService.entity.dataCsvHeaders = fileReader.result
                        .toString()
                        .split('\n')
                        .shift() // Extract first line
                        .split(/[;|,]/)
                        .map(f => f.replace(/[^\w]/g, '')); // Extract columns
                    // console.log('Headers', this.dataFile.dataCsvHeaders);
                    if (this.dataFileService.entity.dataCsvHeaders.length === 1) {
                        this.dataFileService.entity.dataCsvHeaders = ['msisdn'];
                    }
                    this.dataFileService.dataCsvHeaders = Object.assign([], this.dataFileService.entity.dataCsvHeaders);
                };
                // Read the file
                fileReader.readAsText(file_1.slice(0, 1024));
            } else {
                console.error('Cannot detect file type of the file...');
            }
        }
    }

    updateCheckedDataFile(event, item) {
        if (!this.dataFileService.checked[item.id]) {
            this.dataFileService.checked[item.id] = item;
        }
        this.dataFileService.checked[item.id].checked = event.target.checked;
    }
}
