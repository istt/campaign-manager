import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';
import { VasCloudConfigurationService } from './vas-cloud-configuration.service';

@Component({
    selector: 'jhi-vas-cloud-configuration-delete-dialog',
    templateUrl: './vas-cloud-configuration-delete-dialog.component.html'
})
export class VasCloudConfigurationDeleteDialogComponent {
    vasCloudConfiguration: IVasCloudConfiguration;

    constructor(
        private vasCloudConfigurationService: VasCloudConfigurationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.vasCloudConfigurationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'vasCloudConfigurationListModification',
                content: 'Deleted an vasCloudConfiguration'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-vas-cloud-configuration-delete-popup',
    template: ''
})
export class VasCloudConfigurationDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ vasCloudConfiguration }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(VasCloudConfigurationDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.vasCloudConfiguration = vasCloudConfiguration;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
