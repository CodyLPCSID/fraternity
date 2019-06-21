import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeActionComponent } from './home-action.component';
import { FraternitySharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { HomeActionRoute } from 'app/home-action/home-action.route';
import { AddOfferModule } from 'app/action-data/add-offer/add-offer.module';
import { AddRequestModule } from 'app/action-data/add-request/add-request.module';

@NgModule({
    declarations: [HomeActionComponent],
    imports: [FraternitySharedModule, RouterModule.forRoot(HomeActionRoute), CommonModule, AddOfferModule, AddRequestModule]
})
export class HomeActionModule {}
