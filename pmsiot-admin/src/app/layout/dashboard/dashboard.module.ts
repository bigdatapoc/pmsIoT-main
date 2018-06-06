import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgmCoreModule } from '@agm/core';
import { AgmSnazzyInfoWindowModule } from '@agm/snazzy-info-window';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';

import { StatModule } from '../../shared';
import { DataService } from '../../shared/services/data.service';

@NgModule({
    imports: [
        CommonModule,
        DashboardRoutingModule,
        AgmCoreModule.forRoot({
          apiKey: 'AIzaSyBvWWRmC-p1HA0E6Qp-Kfn2EThCeT2gjsA'
        }),
        AgmSnazzyInfoWindowModule,
        StatModule,
        NgbModule.forRoot(),
        FormsModule
    ],
    declarations: [
        DashboardComponent
    ],
    providers: [DataService]
})
export class DashboardModule {}
