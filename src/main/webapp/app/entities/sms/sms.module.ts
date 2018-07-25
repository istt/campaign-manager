import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CampaignManagerSharedModule } from 'app/shared';
import {
    SmsComponent,
    SmsDetailComponent,
    SmsUpdateComponent,
    SmsDeletePopupComponent,
    SmsDeleteDialogComponent,
    smsRoute,
    smsPopupRoute
} from './';

const ENTITY_STATES = [...smsRoute, ...smsPopupRoute];

@NgModule({
    imports: [CampaignManagerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SmsComponent, SmsDetailComponent, SmsUpdateComponent, SmsDeleteDialogComponent, SmsDeletePopupComponent],
    entryComponents: [SmsComponent, SmsUpdateComponent, SmsDeleteDialogComponent, SmsDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CampaignManagerSmsModule {}
