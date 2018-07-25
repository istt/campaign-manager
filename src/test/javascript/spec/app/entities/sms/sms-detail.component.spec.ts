/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CampaignManagerTestModule } from '../../../test.module';
import { SmsDetailComponent } from 'app/entities/sms/sms-detail.component';
import { Sms } from 'app/shared/model/sms.model';

describe('Component Tests', () => {
    describe('Sms Management Detail Component', () => {
        let comp: SmsDetailComponent;
        let fixture: ComponentFixture<SmsDetailComponent>;
        const route = ({ data: of({ sms: new Sms('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CampaignManagerTestModule],
                declarations: [SmsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.sms).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
