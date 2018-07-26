import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IVasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';
import { VasCloudConfigurationService } from './vas-cloud-configuration.service';

@Component({
    selector: 'jhi-vas-cloud-configuration-update',
    templateUrl: './vas-cloud-configuration-update.component.html'
})
export class VasCloudConfigurationUpdateComponent implements OnInit {
    private _vasCloudConfiguration: IVasCloudConfiguration;
    isSaving: boolean;

    constructor(private vasCloudConfigurationService: VasCloudConfigurationService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ vasCloudConfiguration }) => {
            this.vasCloudConfiguration = vasCloudConfiguration;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.vasCloudConfiguration.id !== undefined) {
            this.subscribeToSaveResponse(this.vasCloudConfigurationService.update(this.vasCloudConfiguration));
        } else {
            this.subscribeToSaveResponse(this.vasCloudConfigurationService.create(this.vasCloudConfiguration));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IVasCloudConfiguration>>) {
        result.subscribe(
            (res: HttpResponse<IVasCloudConfiguration>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get vasCloudConfiguration() {
        return this._vasCloudConfiguration;
    }

    set vasCloudConfiguration(vasCloudConfiguration: IVasCloudConfiguration) {
        this._vasCloudConfiguration = vasCloudConfiguration;
    }
}
