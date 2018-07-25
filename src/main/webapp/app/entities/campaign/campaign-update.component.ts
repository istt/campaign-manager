import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils } from 'ng-jhipster';

import { ICampaign } from 'app/shared/model/campaign.model';
import { CampaignService } from './campaign.service';

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

    constructor(private dataUtils: JhiDataUtils, private campaignService: CampaignService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ campaign }) => {
            this.campaign = campaign;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.campaign.createdAt = moment(this.createdAt, DATE_TIME_FORMAT);
        this.campaign.approvedAt = moment(this.approvedAt, DATE_TIME_FORMAT);
        this.campaign.startAt = moment(this.startAt, DATE_TIME_FORMAT);
        this.campaign.expiredAt = moment(this.expiredAt, DATE_TIME_FORMAT);
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
        this.startAt = moment(campaign.startAt).format(DATE_TIME_FORMAT);
        this.expiredAt = moment(campaign.expiredAt).format(DATE_TIME_FORMAT);
    }
}
