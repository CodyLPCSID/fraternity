import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { FraternitySharedModule } from 'app/shared';
import {
    AssociationMemberComponent,
    AssociationMemberDetailComponent,
    AssociationMemberUpdateComponent,
    AssociationMemberDeletePopupComponent,
    AssociationMemberDeleteDialogComponent,
    associationMemberRoute,
    associationMemberPopupRoute
} from './';

const ENTITY_STATES = [...associationMemberRoute, ...associationMemberPopupRoute];

@NgModule({
    imports: [FraternitySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AssociationMemberComponent,
        AssociationMemberDetailComponent,
        AssociationMemberUpdateComponent,
        AssociationMemberDeleteDialogComponent,
        AssociationMemberDeletePopupComponent
    ],
    entryComponents: [
        AssociationMemberComponent,
        AssociationMemberUpdateComponent,
        AssociationMemberDeleteDialogComponent,
        AssociationMemberDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FraternityAssociationMemberModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
