import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { FraternitySharedModule } from 'app/shared';
import {
    HelpRequestComponent,
    HelpRequestDetailComponent,
    HelpRequestUpdateComponent,
    HelpRequestDeletePopupComponent,
    HelpRequestDeleteDialogComponent,
    helpRequestRoute,
    helpRequestPopupRoute
} from './';

const ENTITY_STATES = [...helpRequestRoute, ...helpRequestPopupRoute];

@NgModule({
    imports: [FraternitySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        HelpRequestComponent,
        HelpRequestDetailComponent,
        HelpRequestUpdateComponent,
        HelpRequestDeleteDialogComponent,
        HelpRequestDeletePopupComponent
    ],
    entryComponents: [HelpRequestComponent, HelpRequestUpdateComponent, HelpRequestDeleteDialogComponent, HelpRequestDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FraternityHelpRequestModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
