import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { HelpRequest } from 'app/shared/model/help-request.model';
import { AddRequestService } from './add-request.service';
import { IHelpRequest } from 'app/shared/model/help-request.model';
import { AddRequestComponent } from 'app/action-data/add-request/add-request.component';
import { HelpRequestDeletePopupComponent, HelpRequestDetailComponent, HelpRequestUpdateComponent } from 'app/entities/help-request';

@Injectable({ providedIn: 'root' })
export class HelpRequestResolve implements Resolve<IHelpRequest> {
    constructor(private service: AddRequestService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IHelpRequest> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<HelpRequest>) => response.ok),
                map((helpRequest: HttpResponse<HelpRequest>) => helpRequest.body)
            );
        }
        return of(new HelpRequest());
    }
}

export const addRequestRoute: Routes = [
    {
        path: 'addRequest',
        component: AddRequestComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fraternityApp.helpRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const helpRequestPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: HelpRequestDeletePopupComponent,
        resolve: {
            helpRequest: HelpRequestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'fraternityApp.helpRequest.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
