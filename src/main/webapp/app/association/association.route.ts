import { Injectable } from '@angular/core';
import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';

import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';

import { Association } from 'app/shared/model/association.model';
import { AssociationService } from './association.service';
import { AssociationComponent } from './association.component';
import { IAssociation } from 'app/shared/model/association.model';

export const AssociationRoute: Routes = [
    {
        path: 'associations',
        component: AssociationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Les associations'
        },
        resolve: { pagingParams: JhiResolvePagingParams },
        canActivate: [UserRouteAccessService]
    }
];
