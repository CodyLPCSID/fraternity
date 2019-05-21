import { Component, OnDestroy, OnInit } from '@angular/core';
import { filter, map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { HelpOffer, IHelpOffer } from 'app/shared/model/help-offer.model';
import { Subscription } from 'rxjs';
import { HelpOfferService } from 'app/entities/help-offer';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { Category, ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';
import { ITEMS_PER_PAGE } from 'app/shared';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'jhi-annonce',
    templateUrl: './annonce.component.html',
    styles: []
})
export class AnnonceComponent implements OnInit, OnDestroy {
    helpOffers: IHelpOffer[];
    helpOfferTest: IHelpOffer;
    categories: ICategory[];
    currentAccount: any;
    eventSubscriber: Subscription;
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    selectedCat: number;
    datePick: any;

    constructor(
        protected helpOfferService: HelpOfferService,
        protected categoryService: CategoryService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        private router: Router,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    ngOnInit() {
        this.loadAll();
        this.loadAllCat();
        console.log('loadAll: ' + this.helpOffers);
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInHelpOffers();
    }

    onSubmit(form: Category) {
        console.log(form.typeName.toString());
        this.loadAll();
    }

    search() {
        console.log('date pick : ' + this.datePick);
        this.loadAllSearch(this.selectedCat);
    }

    loadAllSearch(cat: number) {
        this.helpOfferService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                'categoryId.equals': cat
            })
            .pipe(
                filter((res: HttpResponse<IHelpOffer[]>) => res.ok),
                map((res: HttpResponse<IHelpOffer[]>) => res.body)
            )
            .subscribe(
                (res: IHelpOffer[]) => {
                    this.helpOffers = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        console.log(this.helpOffers);
    }

    loadAll() {
        this.selectedCat = 0;
        this.helpOfferService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .pipe(
                filter((res: HttpResponse<IHelpOffer[]>) => res.ok),
                map((res: HttpResponse<IHelpOffer[]>) => res.body)
            )
            .subscribe(
                (res: IHelpOffer[]) => {
                    this.helpOffers = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadAllCat() {
        this.categoryService
            .query()
            .pipe(
                filter((res: HttpResponse<ICategory[]>) => res.ok),
                map((res: HttpResponse<ICategory[]>) => res.body)
            )
            .subscribe(
                (res: ICategory[]) => {
                    this.categories = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
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
        this.router.navigate(['/annonces'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    trackId(index: number, item: IHelpOffer) {
        return item.id;
    }

    registerChangeInHelpOffers() {
        this.eventSubscriber = this.eventManager.subscribe('helpOfferListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
