import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { FraternitySharedModule } from 'app/shared';
import {
    HelpActionComponent,
    HelpActionDetailComponent,
    HelpActionUpdateComponent,
    HelpActionDeletePopupComponent,
    HelpActionDeleteDialogComponent,
    helpActionRoute,
    helpActionPopupRoute
} from './';

const ENTITY_STATES = [...helpActionRoute, ...helpActionPopupRoute];

@NgModule({
    imports: [FraternitySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        HelpActionComponent,
        HelpActionDetailComponent,
        HelpActionUpdateComponent,
        HelpActionDeleteDialogComponent,
        HelpActionDeletePopupComponent
    ],
    entryComponents: [HelpActionComponent, HelpActionUpdateComponent, HelpActionDeleteDialogComponent, HelpActionDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FraternityHelpActionModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
