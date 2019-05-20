import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FraternitySharedModule } from 'app/shared';
import { AssociationComponent } from './association.component';
import { AssociationRoute } from './association.route';

const ENTITY_STATES = [...AssociationRoute];

@NgModule({
    imports: [FraternitySharedModule, RouterModule.forChild(AssociationRoute)],
    declarations: [],
    entryComponents: [AssociationComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AssociationModule {}
