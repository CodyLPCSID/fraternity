import { Routes } from '@angular/router';

import { HomeActionComponent } from './home-action.component';
import { UserRouteAccessService } from 'app/core';

export const HomeActionRoute: Routes = [
    {
        path: 'homeAction',
        component: HomeActionComponent,
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
