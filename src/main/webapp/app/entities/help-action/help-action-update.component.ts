import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IHelpAction } from 'app/shared/model/help-action.model';
import { HelpActionService } from './help-action.service';

@Component({
    selector: 'jhi-help-action-update',
    templateUrl: './help-action-update.component.html'
})
export class HelpActionUpdateComponent implements OnInit {
    helpAction: IHelpAction;
    isSaving: boolean;

    constructor(protected helpActionService: HelpActionService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ helpAction }) => {
            this.helpAction = helpAction;
        });
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
}
