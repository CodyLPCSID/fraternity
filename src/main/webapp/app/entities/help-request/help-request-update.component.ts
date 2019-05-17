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
import { IUser, UserService } from 'app/core';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';

@Component({
    selector: 'jhi-help-request-update',
    templateUrl: './help-request-update.component.html'
})
export class HelpRequestUpdateComponent implements OnInit {
    helpRequest: IHelpRequest;
    isSaving: boolean;

    helpactions: IHelpAction[];

    users: IUser[];

    categories: ICategory[];
    datePostDp: any;
    dateStartDp: any;
    dateEndDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected helpRequestService: HelpRequestService,
        protected helpActionService: HelpActionService,
        protected userService: UserService,
        protected categoryService: CategoryService,
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
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.categoryService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICategory[]>) => response.body)
            )
            .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackCategoryById(index: number, item: ICategory) {
        return item.id;
    }
}
