import { Route } from '@angular/router';
import { AnnonceComponent } from 'app/annonce/annonce.component';
import { UserMgmtComponent } from 'app/admin';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const ANNONCE_ROUTE: Route = {
    path: 'annonces',
    component: AnnonceComponent,
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    data: {
        authorities: [],
        pageTitle: 'annonce.title',
        defaultSort: 'id,asc'
    }
};
