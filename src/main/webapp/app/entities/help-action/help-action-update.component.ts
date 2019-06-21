import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IHelpAction } from 'app/shared/model/help-action.model';
import { HelpActionService } from './help-action.service';
import { IHelpOffer } from 'app/shared/model/help-offer.model';
import { HelpOfferService } from 'app/entities/help-offer';
import { IHelpRequest } from 'app/shared/model/help-request.model';
import { HelpRequestService } from 'app/entities/help-request';

@Component({
    selector: 'jhi-help-action-update',
    templateUrl: './help-action-update.component.html'
})
export class HelpActionUpdateComponent implements OnInit {
    helpAction: IHelpAction;
    isSaving: boolean;

    helpoffers: IHelpOffer[];

    helprequests: IHelpRequest[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected helpActionService: HelpActionService,
        protected helpOfferService: HelpOfferService,
        protected helpRequestService: HelpRequestService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ helpAction }) => {
            this.helpAction = helpAction;
        });
        this.helpOfferService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IHelpOffer[]>) => mayBeOk.ok),
                map((response: HttpResponse<IHelpOffer[]>) => response.body)
            )
            .subscribe((res: IHelpOffer[]) => (this.helpoffers = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.helpRequestService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IHelpRequest[]>) => mayBeOk.ok),
                map((response: HttpResponse<IHelpRequest[]>) => response.body)
            )
            .subscribe((res: IHelpRequest[]) => (this.helprequests = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.helpAction.id !== undefined) {
            this.subscribeToSaveResponse(this.helpActionService.update(this.helpAction));
        } else {
            this.subscribeToSaveResponse(this.helpActionService.create(this.helpAction));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IHelpAction>>) {
        result.subscribe((res: HttpResponse<IHelpAction>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackHelpOfferById(index: number, item: IHelpOffer) {
        return item.id;
    }

    trackHelpRequestById(index: number, item: IHelpRequest) {
        return item.id;
    }
}
