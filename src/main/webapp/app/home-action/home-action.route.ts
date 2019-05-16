import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';

import { AddOfferComponent } from './add-offer.component';
import { UserRouteAccessService } from '../../core';

export const addOfferRoute: Routes = [
    {
        path: 'addOffer',
        component: AddOfferComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Offer'
        },
        canActivate: [UserRouteAccessService]
    }
    // {
    //     path: 'add-offer/new',
    //     component: AddOfferCreateComponent,
    //     resolve: {
    //         helpOffer: HelpOfferResolve
    //     },
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'test'
    //     },
    //     canActivate: [UserRouteAccessService]
    // },
];
