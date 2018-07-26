import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IVasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';
import { Principal } from 'app/core';
import { VasCloudConfigurationService } from './vas-cloud-configuration.service';

@Component({
    selector: 'jhi-vas-cloud-configuration',
    templateUrl: './vas-cloud-configuration.component.html'
})
export class VasCloudConfigurationComponent implements OnInit, OnDestroy {
    vasCloudConfigurations: IVasCloudConfiguration[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private vasCloudConfigurationService: VasCloudConfigurationService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.vasCloudConfigurationService.query().subscribe(
            (res: HttpResponse<IVasCloudConfiguration[]>) => {
                this.vasCloudConfigurations = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInVasCloudConfigurations();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IVasCloudConfiguration) {
        return item.id;
    }

    registerChangeInVasCloudConfigurations() {
        this.eventSubscriber = this.eventManager.subscribe('vasCloudConfigurationListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
