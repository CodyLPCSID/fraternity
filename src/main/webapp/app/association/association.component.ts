import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared';

import { ActivatedRoute, Router } from '@angular/router';

import { IAssociation } from 'app/shared/model/association.model';
import { AccountService } from 'app/core';
import { AssociationService } from './association.service';

@Component({
    selector: 'jhi-association',
    templateUrl: './association.component.html',
    styleUrls: ['association.component.css']
})
export class AssociationComponent implements OnInit, OnDestroy {
    associations: IAssociation[];
    currentAccount: any;
    eventSubscriber: Subscription;
    predicate: any;
    previousPage: any;
    reverse: any;
    page: any;
    routeData: any;
    itemsPerPage: any;
    totalItems: any;
    links: any;

    constructor(
        protected associationService: AssociationService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService,
        private activatedRoute: ActivatedRoute,
        private parseLinks: JhiParseLinks,
        private router: Router
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    loadAll() {
        this.associationService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .pipe(
                filter((res: HttpResponse<IAssociation[]>) => res.ok),
                map((res: HttpResponse<IAssociation[]>) => res.body)
            )
            .subscribe(
                (res: IAssociation[]) => {
                    this.associations = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInAssociations();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAssociation) {
        return item.id;
    }

    registerChangeInAssociations() {
        this.eventSubscriber = this.eventManager.subscribe('associationListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/associations/'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.associations = data;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
