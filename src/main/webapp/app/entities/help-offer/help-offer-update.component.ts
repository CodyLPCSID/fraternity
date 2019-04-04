import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IHelpOffer } from 'app/shared/model/help-offer.model';
import { HelpOfferService } from './help-offer.service';
import { IHelpAction } from 'app/shared/model/help-action.model';
import { HelpActionService } from 'app/entities/help-action';

@Component({
    selector: 'jhi-help-offer-update',
    templateUrl: './help-offer-update.component.html'
})
export class HelpOfferUpdateComponent implements OnInit {
    helpOffer: IHelpOffer;
    isSaving: boolean;

    helpactions: IHelpAction[];
    datePostDp: any;
    dateStartDp: any;
    dateEndDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected helpOfferService: HelpOfferService,
        protected helpActionService: HelpActionService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ helpOffer }) => {
            this.helpOffer = helpOffer;
        });
        this.helpActionService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IHelpAction[]>) => mayBeOk.ok),
                map((response: HttpResponse<IHelpAction[]>) => response.body)
            )
            .subscribe((res: IHelpAction[]) => (this.helpactions = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.helpOffer.id !== undefined) {
            this.subscribeToSaveResponse(this.helpOfferService.update(this.helpOffer));
        } else {
            this.subscribeToSaveResponse(this.helpOfferService.create(this.helpOffer));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IHelpOffer>>) {
        result.subscribe((res: HttpResponse<IHelpOffer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackHelpActionById(index: number, item: IHelpAction) {
        return item.id;
    }
}
