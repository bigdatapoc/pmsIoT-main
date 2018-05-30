import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbCarouselModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { AgmCoreModule } from '@agm/core';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';

import { StatModule } from '../../shared';

@NgModule({
    imports: [
        CommonModule,
        NgbCarouselModule.forRoot(),
        NgbAlertModule.forRoot(),
        DashboardRoutingModule,
        AgmCoreModule.forRoot({
          apiKey: 'AIzaSyBvWWRmC-p1HA0E6Qp-Kfn2EThCeT2gjsA'
        }),
        StatModule
    ],
    declarations: [
        DashboardComponent
    ]
})
export class DashboardModule {}
