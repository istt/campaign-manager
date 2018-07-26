import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';

@Component({
    selector: 'jhi-vas-cloud-configuration-detail',
    templateUrl: './vas-cloud-configuration-detail.component.html'
})
export class VasCloudConfigurationDetailComponent implements OnInit {
    vasCloudConfiguration: IVasCloudConfiguration;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ vasCloudConfiguration }) => {
            this.vasCloudConfiguration = vasCloudConfiguration;
        });
    }

    previousState() {
        window.history.back();
    }
}
