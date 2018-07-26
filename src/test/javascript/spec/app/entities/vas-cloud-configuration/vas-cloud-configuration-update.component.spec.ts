/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CampaignManagerTestModule } from '../../../test.module';
import { VasCloudConfigurationUpdateComponent } from 'app/entities/vas-cloud-configuration/vas-cloud-configuration-update.component';
import { VasCloudConfigurationService } from 'app/entities/vas-cloud-configuration/vas-cloud-configuration.service';
import { VasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';

describe('Component Tests', () => {
    describe('VasCloudConfiguration Management Update Component', () => {
        let comp: VasCloudConfigurationUpdateComponent;
        let fixture: ComponentFixture<VasCloudConfigurationUpdateComponent>;
        let service: VasCloudConfigurationService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CampaignManagerTestModule],
                declarations: [VasCloudConfigurationUpdateComponent]
            })
                .overrideTemplate(VasCloudConfigurationUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(VasCloudConfigurationUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VasCloudConfigurationService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new VasCloudConfiguration('123');
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.vasCloudConfiguration = entity;
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
                    const entity = new VasCloudConfiguration();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.vasCloudConfiguration = entity;
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
