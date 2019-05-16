import { Routes } from '@angular/router';

import { addOfferRoute } from './add-offer/add-offer.route';

const ACTION_ROUTES = [addOfferRoute];

export const actionState: Routes = [
    {
        path: '',
        children: ACTION_ROUTES
    }
];
