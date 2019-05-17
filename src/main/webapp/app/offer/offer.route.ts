import { Injectable } from '@angular/core';
import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';

import { HelpRequest } from 'app/shared/model/help-request.model';
import { OfferService } from './offer.service';
import { OfferComponent } from './offer.component';
import { IHelpRequest } from 'app/shared/model/help-request.model';

export const OfferRoute: Routes = [
    {
        path: 'offer',
        component: OfferComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Les annonces'
        },
        canActivate: [UserRouteAccessService]
    }
];
