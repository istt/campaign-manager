import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CampaignManagerCampaignModule } from './campaign/campaign.module';
import { CampaignManagerSmsModule } from './sms/sms.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        CampaignManagerCampaignModule,
        CampaignManagerSmsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CampaignManagerEntityModule {}
