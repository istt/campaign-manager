/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CampaignManagerTestModule } from '../../../test.module';
import { DataFileUpdateComponent } from 'app/entities/data-file/data-file-update.component';
import { DataFileService } from 'app/entities/data-file/data-file.service';
import { DataFile } from 'app/shared/model/data-file.model';

describe('Component Tests', () => {
    describe('DataFile Management Update Component', () => {
        let comp: DataFileUpdateComponent;
        let fixture: ComponentFixture<DataFileUpdateComponent>;
        let service: DataFileService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CampaignManagerTestModule],
                declarations: [DataFileUpdateComponent]
            })
                .overrideTemplate(DataFileUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DataFileUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataFileService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new DataFile('123');
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.dataFile = entity;
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
                    const entity = new DataFile();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.dataFile = entity;
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
