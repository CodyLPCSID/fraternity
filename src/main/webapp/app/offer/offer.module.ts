import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FraternitySharedModule } from 'app/shared';
import { OfferComponent } from './offer.component';
import { OfferRoute } from './offer.route';

const ENTITY_STATES = [...OfferRoute];

@NgModule({
    imports: [FraternitySharedModule, RouterModule.forChild(OfferRoute)],
    declarations: [],
    entryComponents: [OfferComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OfferModule {}
