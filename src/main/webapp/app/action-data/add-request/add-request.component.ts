import { Component, OnDestroy, OnInit } from '@angular/core';
import { IHelpRequest } from 'app/shared/model/help-request.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { AccountService } from 'app/core';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { HelpRequestService } from 'app/entities/help-request';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
@Component({
    selector: 'jhi-add-request',
    templateUrl: './add-request.component.html',
    styles: []
})
export class AddRequestComponent implements OnInit, OnDestroy {
    helpRequests: IHelpRequest[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected helpRequestService: HelpRequestService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.helpRequestService
            .query()
            .pipe(
                filter((res: HttpResponse<IHelpRequest[]>) => res.ok),
                map((res: HttpResponse<IHelpRequest[]>) => res.body)
            )
            .subscribe(
                (res: IHelpRequest[]) => {
                    this.helpRequests = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInHelpRequests();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IHelpRequest) {
        return item.id;
    }

    registerChangeInHelpRequests() {
        this.eventSubscriber = this.eventManager.subscribe('helpRequestListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
