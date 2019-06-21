import { ActivatedRouteSnapshot, Resolve, Route, RouterStateSnapshot } from '@angular/router';
import { ContactComponent } from 'app/contact/contact.component';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Injectable } from '@angular/core';
import { HelpOffer, IHelpOffer } from 'app/shared/model/help-offer.model';
import { HelpOfferDetailComponent, HelpOfferService } from 'app/entities/help-offer';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { UserRouteAccessService } from 'app/core';

@Injectable({ providedIn: 'root' })
export class HelpOfferResolve implements Resolve<IHelpOffer> {
    constructor(private service: HelpOfferService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IHelpOffer> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<HelpOffer>) => response.ok),
                map((helpOffer: HttpResponse<HelpOffer>) => helpOffer.body)
            );
        }
        return of(new HelpOffer());
    }
}

export const CONTACT_ROUTE: Route = {
    path: 'contact/:id',
    component: ContactComponent,
    resolve: {
        helpOffer: HelpOfferResolve
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'fraternityApp.helpOffer.home.title'
    },
    canActivate: [UserRouteAccessService]
};
