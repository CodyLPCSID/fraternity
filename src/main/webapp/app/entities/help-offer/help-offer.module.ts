import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { FraternitySharedModule } from 'app/shared';
import {
    HelpOfferComponent,
    HelpOfferDetailComponent,
    HelpOfferUpdateComponent,
    HelpOfferDeletePopupComponent,
    HelpOfferDeleteDialogComponent,
    helpOfferRoute,
    helpOfferPopupRoute
} from './';

const ENTITY_STATES = [...helpOfferRoute, ...helpOfferPopupRoute];

@NgModule({
    imports: [FraternitySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        HelpOfferComponent,
        HelpOfferDetailComponent,
        HelpOfferUpdateComponent,
        HelpOfferDeleteDialogComponent,
        HelpOfferDeletePopupComponent
    ],
    entryComponents: [HelpOfferComponent, HelpOfferUpdateComponent, HelpOfferDeleteDialogComponent, HelpOfferDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FraternityHelpOfferModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
