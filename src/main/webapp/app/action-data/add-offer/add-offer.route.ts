import { Route, Routes } from '@angular/router';

import { AddOfferComponent } from './add-offer.component';

export const addOfferRoute: Route = {
    path: 'addOffer',
    component: AddOfferComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'activate.title'
    }
};
