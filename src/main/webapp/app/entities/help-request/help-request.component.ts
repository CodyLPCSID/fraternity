import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IHelpRequest } from 'app/shared/model/help-request.model';
import { AccountService } from 'app/core';
import { HelpRequestService } from './help-request.service';

@Component({
    selector: 'jhi-help-request',
    templateUrl: './help-request.component.html'
})
export class HelpRequestComponent implements OnInit, OnDestroy {
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
