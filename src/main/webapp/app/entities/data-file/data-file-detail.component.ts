import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IDataFile } from 'app/shared/model/data-file.model';

@Component({
    selector: 'jhi-data-file-detail',
    templateUrl: './data-file-detail.component.html'
})
export class DataFileDetailComponent implements OnInit {
    dataFile: IDataFile;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dataFile }) => {
            this.dataFile = dataFile;
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
}
