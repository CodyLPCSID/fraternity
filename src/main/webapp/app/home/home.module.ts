import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FraternitySharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import { AgmCoreModule } from '@agm/core';

@NgModule({
    imports: [
        FraternitySharedModule,
        RouterModule.forChild([HOME_ROUTE]),
        AgmCoreModule.forRoot({
            apiKey: 'AIzaSyDZqKzmezcDpiI5zUvqxDfA0JTSMhJFFp8'
        })
    ],
    declarations: [HomeComponent],
    schemas: []
})
export class FraternityHomeModule {}
