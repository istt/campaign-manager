/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CampaignManagerTestModule } from '../../../test.module';
import { SmsUpdateComponent } from 'app/entities/sms/sms-update.component';
import { SmsService } from 'app/entities/sms/sms.service';
import { Sms } from 'app/shared/model/sms.model';

describe('Component Tests', () => {
    describe('Sms Management Update Component', () => {
        let comp: SmsUpdateComponent;
        let fixture: ComponentFixture<SmsUpdateComponent>;
        let service: SmsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CampaignManagerTestModule],
                declarations: [SmsUpdateComponent]
            })
                .overrideTemplate(SmsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Sms('123');
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.sms = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Sms();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.sms = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
