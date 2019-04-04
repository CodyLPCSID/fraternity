import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IHelpRequest } from 'app/shared/model/help-request.model';
import { HelpRequestService } from './help-request.service';
import { IHelpAction } from 'app/shared/model/help-action.model';
import { HelpActionService } from 'app/entities/help-action';

@Component({
    selector: 'jhi-help-request-update',
    templateUrl: './help-request-update.component.html'
})
export class HelpRequestUpdateComponent implements OnInit {
    helpRequest: IHelpRequest;
    isSaving: boolean;

    helpactions: IHelpAction[];
    datePostDp: any;
    dateStartDp: any;
    dateEndDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected helpRequestService: HelpRequestService,
        protected helpActionService: HelpActionService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ helpRequest }) => {
            this.helpRequest = helpRequest;
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
        if (this.helpRequest.id !== undefined) {
            this.subscribeToSaveResponse(this.helpRequestService.update(this.helpRequest));
        } else {
            this.subscribeToSaveResponse(this.helpRequestService.create(this.helpRequest));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IHelpRequest>>) {
        result.subscribe((res: HttpResponse<IHelpRequest>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
