import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IHelpRequest } from 'app/shared/model/help-request.model';
import { AccountService } from 'app/core';
import { OfferService } from './offer.service';

@Component({
    selector: 'jhi-offer',
    templateUrl: './offer.component.html',
    styleUrls: ['offer.component.css']
})
export class OfferComponent implements OnInit, OnDestroy {
    helpRequests: IHelpRequest[];
    currentAccount: any;
    eventSubscriber: Subscription;
    page = 1;
    previousPage = 1;

    constructor(
        protected offerService: OfferService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            // this.getSearchFilms(this.value, this.page);
        }
    }

    loadAll() {
        this.offerService
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
