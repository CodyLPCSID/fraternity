import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { FraternitySharedModule } from 'app/shared';
import {
    AssociationComponent,
    AssociationDetailComponent,
    AssociationUpdateComponent,
    AssociationDeletePopupComponent,
    AssociationDeleteDialogComponent,
    associationRoute,
    associationPopupRoute
} from './';

const ENTITY_STATES = [...associationRoute, ...associationPopupRoute];

@NgModule({
    imports: [FraternitySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AssociationComponent,
        AssociationDetailComponent,
        AssociationUpdateComponent,
        AssociationDeleteDialogComponent,
        AssociationDeletePopupComponent
    ],
    entryComponents: [AssociationComponent, AssociationUpdateComponent, AssociationDeleteDialogComponent, AssociationDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FraternityAssociationModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
