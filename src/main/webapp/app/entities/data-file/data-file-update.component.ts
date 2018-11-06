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
    dataCsvHeaders: string[] = [];

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
        // Try to extract headers from current open file
        if (event && event.target.files && event.target.files[0]) {
            const file_1 = event.target.files[0];
            this.dataFile.name = file_1.name;
            // if (isImage && !/^image\//.test(file_1.type)) {
            //     return;
            // }
            // this.toBase64(file_1, function (base64Data) {
            //     entity[field] = base64Data;
            //     entity[field + "ContentType"] = file_1.type;
            // });
            console.log(file_1.type);
            if (file_1.type.match(/text/i)) {
                const fileReader = new FileReader();
                fileReader.onload = e => {
                    this.dataFile.dataCsvPreview = fileReader.result.toString(); // first line
                    // console.log('First line', this.dataFile.dataCsvPreview);
                    this.dataFile.dataCsvHeaders = fileReader.result
                        .toString()
                        .split('\n')
                        .shift() // Extract first line
                        .split(/[;|,]/)
                        .map(f => f.trim()); // Extract columns
                    // console.log('Headers', this.dataFile.dataCsvHeaders);
                    this.dataCsvHeaders = Object.assign([], this.dataFile.dataCsvHeaders);
                };
                // Read the file
                fileReader.readAsText(file_1.slice(0, 1024));
            }
        }
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
