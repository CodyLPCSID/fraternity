import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'association',
                loadChildren: './association/association.module#FraternityAssociationModule'
            },
            {
                path: 'association-member',
                loadChildren: './association-member/association-member.module#FraternityAssociationMemberModule'
            },
            {
                path: 'help-offer',
                loadChildren: './help-offer/help-offer.module#FraternityHelpOfferModule'
            },
            {
                path: 'help-request',
                loadChildren: './help-request/help-request.module#FraternityHelpRequestModule'
            },
            {
                path: 'help-action',
                loadChildren: './help-action/help-action.module#FraternityHelpActionModule'
            },
            {
                path: 'category',
                loadChildren: './category/category.module#FraternityCategoryModule'
            },
            {
                path: 'help-offer',
                loadChildren: './help-offer/help-offer.module#FraternityHelpOfferModule'
            },
            {
                path: 'help-offer',
                loadChildren: './help-offer/help-offer.module#FraternityHelpOfferModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FraternityEntityModule {}
