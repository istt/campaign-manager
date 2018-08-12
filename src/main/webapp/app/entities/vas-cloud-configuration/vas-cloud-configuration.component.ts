import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IVasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';
import { Principal } from 'app/core';
import { VasCloudConfigurationService } from './vas-cloud-configuration.service';
// Extra component
import { DataFileService } from '../data-file';
import { DataFile } from 'app/shared/model/data-file.model';
import { NgbModal, NgbModalRef, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-vas-cloud-configuration',
    templateUrl: './vas-cloud-configuration.component.html'
})
export class VasCloudConfigurationComponent implements OnInit, OnDestroy {
    vasCloudConfigurations: IVasCloudConfiguration[];
    currentAccount: any;
    eventSubscriber: Subscription;
    isSaving = false;
    modalRef: NgbModalRef;

    constructor(
        public vasCloudConfigurationService: VasCloudConfigurationService,
        public dataFileService: DataFileService,
        private modalService: NgbModal,
        private dataUtils: JhiDataUtils,
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

    export() {
        this.dataFileService.exportData(
            'vas-cloud-configuration' + '.' + new Date().toISOString().substr(0, 10) + '.json',
            'api/vas-cloud-configurations'
        );
    }

    // Data File Modal handle
    openModal(modalContent) {
        this.dataFileService.entity = new DataFile();
        this.modalRef = this.modalService.open(modalContent);
        this.modalRef.result.then(result => console.log(`Closed with: ${result}`), reason => console.error(reason));
    }

    importData() {
        console.log('Prepare to save data file...');
        this.isSaving = true;
        this.dataFileService.importData(this.dataFileService.entity, 'api/import/vas-cloud-configurations').subscribe(
            res => {
                console.log('Successfully submit data file', res);
                this.dataFileService.entity = res.body;
                this.isSaving = false;
                this.loadAll();
                this.modalRef.close();
            },
            err => this.onError(err)
        );
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
            this.dataFileService.entity.name = file_1.name;
            if (file_1.type.match(/text/i)) {
                const fileReader = new FileReader();
                fileReader.onload = e => {
                    this.dataFileService.entity.dataCsvPreview = fileReader.result; // first line
                    // console.log('First line', this.dataFile.dataCsvPreview);
                    this.dataFileService.entity.dataCsvHeaders = fileReader.result
                        .split('\n')
                        .shift() // Extract first line
                        .split(/[;|,]/)
                        .map(f => f.replace(/[^\w]/g, '')); // Extract columns
                    // console.log('Headers', this.dataFile.dataCsvHeaders);
                    if (this.dataFileService.entity.dataCsvHeaders.length === 1) {
                        this.dataFileService.entity.dataCsvHeaders = ['msisdn'];
                    }
                    this.dataFileService.dataCsvHeaders = Object.assign([], this.dataFileService.entity.dataCsvHeaders);
                };
                // Read the file
                fileReader.readAsText(file_1.slice(0, 1024));
            } else {
                console.error('Cannot detect file type of the file...');
            }
        }
    }
}
