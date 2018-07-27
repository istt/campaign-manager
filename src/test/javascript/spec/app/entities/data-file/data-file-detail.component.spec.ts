/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CampaignManagerTestModule } from '../../../test.module';
import { DataFileDetailComponent } from 'app/entities/data-file/data-file-detail.component';
import { DataFile } from 'app/shared/model/data-file.model';

describe('Component Tests', () => {
    describe('DataFile Management Detail Component', () => {
        let comp: DataFileDetailComponent;
        let fixture: ComponentFixture<DataFileDetailComponent>;
        const route = ({ data: of({ dataFile: new DataFile('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CampaignManagerTestModule],
                declarations: [DataFileDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DataFileDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DataFileDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.dataFile).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
