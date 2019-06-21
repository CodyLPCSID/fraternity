import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FraternitySharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { addRequestRoute } from 'app/action-data/add-request/add-request.route';
import { AddRequestComponent } from 'app/action-data/add-request/add-request.component';

@NgModule({
    declarations: [AddRequestComponent],
    imports: [HttpClientModule, FraternitySharedModule, RouterModule.forRoot(addRequestRoute), CommonModule],
    exports: [AddRequestComponent]
})
export class AddRequestModule {}
