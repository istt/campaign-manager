import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ICampaign } from 'app/shared/model/campaign.model';
import { JhiTrackerService } from 'app/core';

@Component({
    selector: 'jhi-campaign-detail',
    templateUrl: './campaign-detail.component.html'
})
export class CampaignDetailComponent implements OnInit, OnDestroy {
    campaign: ICampaign;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute, private trackerService: JhiTrackerService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ campaign }) => {
            this.campaign = campaign;
            this.trackerService.subscribe('/topic/campaign/' + campaign.id);
            this.trackerService.receive().subscribe(activity => Object.assign(campaign.stats, activity));
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
}
