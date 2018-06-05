import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbCarouselModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { AgmCoreModule } from '@agm/core';
import { AgmSnazzyInfoWindowModule } from '@agm/snazzy-info-window';
import { FormsModule } from '@angular/forms';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';

import { StatModule } from '../../shared';
import { DataService } from '../../shared/services/data.service';

@NgModule({
    imports: [
        CommonModule,
        NgbCarouselModule.forRoot(),
        NgbAlertModule.forRoot(),
        DashboardRoutingModule,
        AgmCoreModule.forRoot({
          apiKey: 'AIzaSyBvWWRmC-p1HA0E6Qp-Kfn2EThCeT2gjsA'
        }),
        AgmSnazzyInfoWindowModule,
        StatModule,
        FormsModule
    ],
    declarations: [
        DashboardComponent
    ],
    providers: [DataService]
})
export class DashboardModule {}
