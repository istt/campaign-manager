/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CampaignManagerTestModule } from '../../../test.module';
import { VasCloudConfigurationDetailComponent } from 'app/entities/vas-cloud-configuration/vas-cloud-configuration-detail.component';
import { VasCloudConfiguration } from 'app/shared/model/vas-cloud-configuration.model';

describe('Component Tests', () => {
    describe('VasCloudConfiguration Management Detail Component', () => {
        let comp: VasCloudConfigurationDetailComponent;
        let fixture: ComponentFixture<VasCloudConfigurationDetailComponent>;
        const route = ({ data: of({ vasCloudConfiguration: new VasCloudConfiguration('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CampaignManagerTestModule],
                declarations: [VasCloudConfigurationDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(VasCloudConfigurationDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(VasCloudConfigurationDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.vasCloudConfiguration).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
