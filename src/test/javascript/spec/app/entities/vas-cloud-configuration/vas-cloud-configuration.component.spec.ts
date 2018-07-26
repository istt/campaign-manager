/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CampaignManagerTestModule } from '../../../test.module';
import { VasCloudConfigurationComponent } from 'app/entities/vas-cloud-configuration/vas-cloud-configuration.component';
import { VasCloudConfigurationService } from 'app/entities/vas-cloud-configuration/vas-cloud-configuration.service';
import { VasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';

describe('Component Tests', () => {
    describe('VasCloudConfiguration Management Component', () => {
        let comp: VasCloudConfigurationComponent;
        let fixture: ComponentFixture<VasCloudConfigurationComponent>;
        let service: VasCloudConfigurationService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CampaignManagerTestModule],
                declarations: [VasCloudConfigurationComponent],
                providers: []
            })
                .overrideTemplate(VasCloudConfigurationComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(VasCloudConfigurationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VasCloudConfigurationService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new VasCloudConfiguration('123')],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.vasCloudConfigurations[0]).toEqual(jasmine.objectContaining({ id: '123' }));
        });
    });
});
