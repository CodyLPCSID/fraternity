import { Component, OnInit } from '@angular/core';
import { JhiDataUtils } from 'ng-jhipster';
import { ActivatedRoute } from '@angular/router';
import { IHelpOffer } from 'app/shared/model/help-offer.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-contact',
    templateUrl: './contact.component.html',
    styles: []
})
export class ContactComponent implements OnInit {
    helpOffer: IHelpOffer;
    closeResult: string;
    message: string;

    constructor(private modalService: NgbModal, protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ helpOffer }) => {
            this.helpOffer = helpOffer;
        });
    }

    onKey(message: any) {
        this.message = message + '';
    }

    openVerticallyCentered(content) {
        this.modalService.open(content, { centered: true });
    }
}
