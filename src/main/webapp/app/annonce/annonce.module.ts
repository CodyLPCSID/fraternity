import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FraternitySharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { HOME_ROUTE } from 'app/home';
import { AnnonceComponent } from 'app/annonce/annonce.component';
import { ANNONCE_ROUTE } from 'app/annonce/annonce.route';

@NgModule({
    declarations: [AnnonceComponent],
    imports: [FraternitySharedModule, RouterModule.forChild([ANNONCE_ROUTE])]
})
export class AnnonceModule {}
