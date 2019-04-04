import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAssociation } from 'app/shared/model/association.model';
import { AssociationService } from './association.service';
import { IAssociationMember } from 'app/shared/model/association-member.model';
import { AssociationMemberService } from 'app/entities/association-member';

@Component({
    selector: 'jhi-association-update',
    templateUrl: './association-update.component.html'
})
export class AssociationUpdateComponent implements OnInit {
    association: IAssociation;
    isSaving: boolean;

    associationmembers: IAssociationMember[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected associationService: AssociationService,
        protected associationMemberService: AssociationMemberService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ association }) => {
            this.association = association;
        });
        this.associationMemberService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAssociationMember[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAssociationMember[]>) => response.body)
            )
            .subscribe(
                (res: IAssociationMember[]) => (this.associationmembers = res),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.association.id !== undefined) {
            this.subscribeToSaveResponse(this.associationService.update(this.association));
        } else {
            this.subscribeToSaveResponse(this.associationService.create(this.association));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssociation>>) {
        result.subscribe((res: HttpResponse<IAssociation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAssociationMemberById(index: number, item: IAssociationMember) {
        return item.id;
    }
}
