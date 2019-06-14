import { Component, ElementRef, OnInit } from '@angular/core';
import { AddOfferService } from '../add-offer/add-offer.service';
import { IHelpOffer } from 'app/shared/model/help-offer.model';
import { Observable, Subscription } from 'rxjs';
import { HelpOfferService } from 'app/entities/help-offer';
import { JhiAlertService, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { Account, AccountService, IUser, UserService } from 'app/core';
import { filter, map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IHelpAction } from 'app/shared/model/help-action.model';
import { ICategory } from 'app/shared/model/category.model';
import { HelpActionService } from 'app/entities/help-action';
import { CategoryService } from 'app/entities/category';
import { ActivatedRoute } from '@angular/router';
import moment = require('moment');

@Component({
    selector: 'jhi-add-offer',
    templateUrl: './add-offer.component.html',
    styles: []
})
export class AddOfferComponent implements OnInit {
    fileData: File = null;

    account: Account;
    public isCollapsed = false;

    helpOffer: IHelpOffer;
    isSaving: boolean;

    helpactions: IHelpAction[];

    users: IUser[];

    categories: ICategory[];
    datePostDp: any;
    dateStartDp: any;
    dateEndDp: any;

    constructor(
        protected dataUtils: JhiDataUtils,
        private accountService: AccountService,
        protected jhiAlertService: JhiAlertService,
        protected helpOfferService: HelpOfferService,
        protected helpActionService: HelpActionService,
        protected userService: UserService,
        protected elementRef: ElementRef,
        protected categoryService: CategoryService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.accountService.identity().then((account: Account) => {
            this.account = account;
            // @ts-ignore
            this.helpOffer.user = account;
        });
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

    handleFileInput(files: FileList) {
        this.fileData = files.item(0);
        console.log((this.fileData = files.item(0)));
    }

    // uploadFileToActivity() {
    //     this.helpOfferService.postFile(this.helpOfferService).subscribe(data => {
    //
    //     }, error => {
    //         console.log(error);
    //     });
    // }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.helpOffer, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    clearDate() {
        this.isCollapsed = !this.isCollapsed;

        if (this.isCollapsed != true) {
            this.datePostDp = ' ';
            this.dateEndDp = ' ';
        }
    }
    save() {
        const today = new Date();
        this.datePostDp = today;
        console.log('date' + today);
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackCategoryById(index: number, item: ICategory) {
        return item.id;
    }
}
