import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from './location.service';
import { IHelpOffer } from 'app/shared/model/help-offer.model';
import { HelpOfferService } from 'app/entities/help-offer';

@Component({
    selector: 'jhi-location-update',
    templateUrl: './location-update.component.html'
})
export class LocationUpdateComponent implements OnInit {
    location: ILocation;
    isSaving: boolean;

    helpoffers: IHelpOffer[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected locationService: LocationService,
        protected helpOfferService: HelpOfferService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ location }) => {
            this.location = location;
        });
        this.helpOfferService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IHelpOffer[]>) => mayBeOk.ok),
                map((response: HttpResponse<IHelpOffer[]>) => response.body)
            )
            .subscribe((res: IHelpOffer[]) => (this.helpoffers = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.location.id !== undefined) {
            this.subscribeToSaveResponse(this.locationService.update(this.location));
        } else {
            this.subscribeToSaveResponse(this.locationService.create(this.location));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocation>>) {
        result.subscribe((res: HttpResponse<ILocation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackHelpOfferById(index: number, item: IHelpOffer) {
        return item.id;
    }
}
