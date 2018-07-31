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
    csvData: string[][];

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dataFile }) => {
            this.dataFile = dataFile;
            // if (this.dataFile.dataContentType.match(/text/i)) {
            //   const fileReader = new FileReader();
            //   fileReader.onload = e => {
            //     this.csvData = fileReader.result
            //     .split('\n')
            //     .map(r => r.split(/[;|,]/));
            //   };
            //   // To support IE and Edge
            //   const byteCharacters = atob(this.dataFile.data);
            //   const byteNumbers = new Array(byteCharacters.length);
            //   // for (let i = 0; i < byteCharacters.length; i++) {
            //   for (let i = 0; i < 1024; i++) {
            //       byteNumbers[i] = byteCharacters.charCodeAt(i);
            //   }
            //   const byteArray = new Uint8Array(byteNumbers);
            //   const blob = new Blob([byteArray], {
            //       type: this.dataFile.dataContentType
            //   });
            //   fileReader.readAsText(blob);
            // }
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
