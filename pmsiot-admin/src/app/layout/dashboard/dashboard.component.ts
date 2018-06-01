import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';
import { DataService } from '../../shared/services/data.service';
import { GoogleMapsAPIWrapper, MapsAPILoader, AgmMap, LatLngBounds, LatLngBoundsLiteral} from '@agm/core';

declare var google: any;

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    animations: [routerTransition()]
})
export class DashboardComponent implements OnInit {
    
    center = {
        lat: 28.53590728969859,
        lng: 77.3436963558197,
        zoom: 17
    };
    
    markerUrl: string = 'assets/images/marker-person.png';
    markers = [];
    boundaries = [];

    constructor(private dataService: DataService, private loader: MapsAPILoader) {
    }

    ngOnInit() {
        this.getAllUsers();
        this.getLocations();
    }

    /**
     * get all user locations
     */
    getAllUsers() {
        this.dataService.getAllUsers()
                            .subscribe((response) => {
                                if (response.status == 'Success') {
                                    this.markers = response.data;
                                }
                            });
    }

   

    /**
     * method to get all geofence locations
     */
    getLocations() {
        this.dataService.getGeoFenceLocations()
                    .subscribe((response) => {
                        if (response.status == 'Success') {
                            response.data.forEach(element => {
                                this.boundaries.push(element.boudary)
                            });

                            this.loadBoundaries();
                        }
                    });
    }

    /**
     * load geofence in map
     */
    loadBoundaries() {
        this.loader.load().then(() => {
            this.boundaries.forEach((arr) => {
                arr.forEach((element, index) => {
                    arr[index]['lat'] = element.latitude;
                    arr[index]['lng'] = element.longitude;
                    delete arr[index]['latitude'];
                    delete arr[index]['longitude'];
                });
            });
        });
    }
    

}
