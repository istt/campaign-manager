import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';
// Extra components
import { VasCloudConfigurationService } from './vas-cloud-configuration.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils } from 'ng-jhipster';

import { ISms, Sms } from 'app/shared/model/sms.model';

@Component({
    selector: 'jhi-vas-cloud-configuration-detail',
    templateUrl: './vas-cloud-configuration-detail.component.html'
})
export class VasCloudConfigurationDetailComponent implements OnInit {
    vasCloudConfiguration: IVasCloudConfiguration;
    sms: ISms;
    isSaving: boolean;
    submitAt: string;
    expiredAt: string;
    deliveredAt: string;

    constructor(public vasCloudConfigurationService: VasCloudConfigurationService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ vasCloudConfiguration }) => {
            this.vasCloudConfiguration = vasCloudConfiguration;
        });
        this.sms = new Sms();
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.sms.submitAt = moment();
        this.sms.state = 0;
        this.vasCloudConfigurationService
            .submit(this.vasCloudConfiguration.id, this.sms)
            .subscribe((res: HttpResponse<ISms>) => Object.assign(this.sms, res.body), err => this.onSaveError());
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
