import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CampaignManagerSharedModule } from 'app/shared';
import {
    VasCloudConfigurationComponent,
    VasCloudConfigurationDetailComponent,
    VasCloudConfigurationUpdateComponent,
    VasCloudConfigurationDeletePopupComponent,
    VasCloudConfigurationDeleteDialogComponent,
    vasCloudConfigurationRoute,
    vasCloudConfigurationPopupRoute
} from './';

const ENTITY_STATES = [...vasCloudConfigurationRoute, ...vasCloudConfigurationPopupRoute];

@NgModule({
    imports: [CampaignManagerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        VasCloudConfigurationComponent,
        VasCloudConfigurationDetailComponent,
        VasCloudConfigurationUpdateComponent,
        VasCloudConfigurationDeleteDialogComponent,
        VasCloudConfigurationDeletePopupComponent
    ],
    entryComponents: [
        VasCloudConfigurationComponent,
        VasCloudConfigurationUpdateComponent,
        VasCloudConfigurationDeleteDialogComponent,
        VasCloudConfigurationDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CampaignManagerVasCloudConfigurationModule {}
