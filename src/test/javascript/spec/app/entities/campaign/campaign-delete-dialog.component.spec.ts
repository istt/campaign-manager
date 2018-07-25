/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CampaignManagerTestModule } from '../../../test.module';
import { CampaignDeleteDialogComponent } from 'app/entities/campaign/campaign-delete-dialog.component';
import { CampaignService } from 'app/entities/campaign/campaign.service';

describe('Component Tests', () => {
    describe('Campaign Management Delete Component', () => {
        let comp: CampaignDeleteDialogComponent;
        let fixture: ComponentFixture<CampaignDeleteDialogComponent>;
        let service: CampaignService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CampaignManagerTestModule],
                declarations: [CampaignDeleteDialogComponent]
            })
                .overrideTemplate(CampaignDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CampaignDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CampaignService);
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
