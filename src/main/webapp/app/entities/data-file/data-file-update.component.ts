import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { IDataFile } from 'app/shared/model/data-file.model';
import { DataFileService } from './data-file.service';

@Component({
    selector: 'jhi-data-file-update',
    templateUrl: './data-file-update.component.html'
})
export class DataFileUpdateComponent implements OnInit {
    private _dataFile: IDataFile;
    isSaving: boolean;

    constructor(private dataUtils: JhiDataUtils, private dataFileService: DataFileService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
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

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.dataFile.id !== undefined) {
            this.subscribeToSaveResponse(this.dataFileService.update(this.dataFile));
        } else {
            this.subscribeToSaveResponse(this.dataFileService.create(this.dataFile));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDataFile>>) {
        result.subscribe((res: HttpResponse<IDataFile>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get dataFile() {
        return this._dataFile;
    }

    set dataFile(dataFile: IDataFile) {
        this._dataFile = dataFile;
    }
}
