import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAssociation } from 'app/shared/model/association.model';
import { AccountService } from 'app/core';
import { AssociationService } from './association.service';

@Component({
    selector: 'jhi-association',
    templateUrl: './association.component.html'
})
export class AssociationComponent implements OnInit, OnDestroy {
    associations: IAssociation[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected associationService: AssociationService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.associationService
            .query()
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

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
