import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter, NgbTimeAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { NgbTimeStringAdapter } from './util/timepicker-adapter';
import { CampaignManagerSharedLibsModule, CampaignManagerSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
@NgModule({
    imports: [CampaignManagerSharedLibsModule, CampaignManagerSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }, { provide: NgbTimeAdapter, useClass: NgbTimeStringAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [CampaignManagerSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CampaignManagerSharedModule {}
