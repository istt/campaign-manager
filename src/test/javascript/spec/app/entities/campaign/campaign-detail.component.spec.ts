/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CampaignManagerTestModule } from '../../../test.module';
import { CampaignDetailComponent } from 'app/entities/campaign/campaign-detail.component';
import { Campaign } from 'app/shared/model/campaign.model';

describe('Component Tests', () => {
    describe('Campaign Management Detail Component', () => {
        let comp: CampaignDetailComponent;
        let fixture: ComponentFixture<CampaignDetailComponent>;
        const route = ({ data: of({ campaign: new Campaign('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CampaignManagerTestModule],
                declarations: [CampaignDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CampaignDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CampaignDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.campaign).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
