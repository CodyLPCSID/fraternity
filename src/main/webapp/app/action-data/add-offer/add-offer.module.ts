import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddOfferComponent } from 'app/action-data/add-offer/add-offer.component';
import { RouterModule } from '@angular/router';
import { FraternitySharedModule } from 'app/shared';
import { addOfferRoute } from 'app/action-data/add-offer/add-offer.route';

@NgModule({
    declarations: [AddOfferComponent],
    imports: [FraternitySharedModule, RouterModule.forRoot([addOfferRoute]), CommonModule],
    exports: [AddOfferComponent]
})
export class AddOfferModule {}
