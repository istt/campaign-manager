/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CampaignManagerTestModule } from '../../../test.module';
import { DataFileDeleteDialogComponent } from 'app/entities/data-file/data-file-delete-dialog.component';
import { DataFileService } from 'app/entities/data-file/data-file.service';

describe('Component Tests', () => {
    describe('DataFile Management Delete Component', () => {
        let comp: DataFileDeleteDialogComponent;
        let fixture: ComponentFixture<DataFileDeleteDialogComponent>;
        let service: DataFileService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CampaignManagerTestModule],
                declarations: [DataFileDeleteDialogComponent]
            })
                .overrideTemplate(DataFileDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DataFileDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataFileService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete('123');
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith('123');
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
