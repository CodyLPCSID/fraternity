import { NgModule } from '@angular/core';
import { FraternitySharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { CONTACT_ROUTE } from 'app/contact/contact.route';
import { ContactComponent } from 'app/contact/contact.component';

@NgModule({
    declarations: [ContactComponent],
    imports: [FraternitySharedModule, RouterModule.forChild([CONTACT_ROUTE])]
})
export class ContactModule {}
