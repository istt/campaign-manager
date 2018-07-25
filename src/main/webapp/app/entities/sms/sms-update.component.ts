import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils } from 'ng-jhipster';

import { ISms } from 'app/shared/model/sms.model';
import { SmsService } from './sms.service';

@Component({
    selector: 'jhi-sms-update',
    templateUrl: './sms-update.component.html'
})
export class SmsUpdateComponent implements OnInit {
    private _sms: ISms;
    isSaving: boolean;
    submitAt: string;
    expiredAt: string;
    deliveredAt: string;

    constructor(private dataUtils: JhiDataUtils, private smsService: SmsService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sms }) => {
            this.sms = sms;
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
        this.sms.submitAt = moment(this.submitAt, DATE_TIME_FORMAT);
        this.sms.expiredAt = moment(this.expiredAt, DATE_TIME_FORMAT);
        this.sms.deliveredAt = moment(this.deliveredAt, DATE_TIME_FORMAT);
        if (this.sms.id !== undefined) {
            this.subscribeToSaveResponse(this.smsService.update(this.sms));
        } else {
            this.subscribeToSaveResponse(this.smsService.create(this.sms));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISms>>) {
        result.subscribe((res: HttpResponse<ISms>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get sms() {
        return this._sms;
    }

    set sms(sms: ISms) {
        this._sms = sms;
        this.submitAt = moment(sms.submitAt).format(DATE_TIME_FORMAT);
        this.expiredAt = moment(sms.expiredAt).format(DATE_TIME_FORMAT);
        this.deliveredAt = moment(sms.deliveredAt).format(DATE_TIME_FORMAT);
    }
}
