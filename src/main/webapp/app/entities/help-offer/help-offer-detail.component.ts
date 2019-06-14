import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IHelpOffer } from 'app/shared/model/help-offer.model';

@Component({
    selector: 'jhi-help-offer-detail',
    templateUrl: './help-offer-detail.component.html'
})
export class HelpOfferDetailComponent implements OnInit {
    helpOffer: IHelpOffer;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ helpOffer }) => {
            this.helpOffer = helpOffer;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
