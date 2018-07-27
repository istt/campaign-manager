import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CampaignManagerSharedModule } from 'app/shared';
import {
    DataFileComponent,
    DataFileDetailComponent,
    DataFileUpdateComponent,
    DataFileDeletePopupComponent,
    DataFileDeleteDialogComponent,
    dataFileRoute,
    dataFilePopupRoute
} from './';

const ENTITY_STATES = [...dataFileRoute, ...dataFilePopupRoute];

@NgModule({
    imports: [CampaignManagerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DataFileComponent,
        DataFileDetailComponent,
        DataFileUpdateComponent,
        DataFileDeleteDialogComponent,
        DataFileDeletePopupComponent
    ],
    entryComponents: [DataFileComponent, DataFileUpdateComponent, DataFileDeleteDialogComponent, DataFileDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CampaignManagerDataFileModule {}
