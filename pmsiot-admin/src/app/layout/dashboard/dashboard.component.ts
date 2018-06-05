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
    
    defaultOptions = {
        lat: 28.53590728969859,
        lng: 77.3436963558197,
        zoom: 17,
        polygonLat: 0,
        polygonLng: 0,
        isPolygonOpen: false
    };
    
    searchKey = null;
    userMap;
    markerUrl: string = 'assets/images/marker-person.png';
    markerUrlOffline: string = 'assets/images/marker-person.png';
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
        this.searchKey = '';
        this.dataService.getAllUsers()
                            .subscribe((response) => {
                                if (response.status == 'Success') {
                                    this.markers = response.data;
                                    this.userMap.fitBounds(this.findStoresBounds());
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
                            this.loadBoundaries(response.data);
                        }
                    });
    }

    /**
     * load geofence in map
     */
    loadBoundaries(data) {
        this.loader.load().then(() => {

            let boundaries = [];

            for (var i = 0; i < data.length; i++) {

                let arr = data[i].boundary;
                boundaries[i] = { points: [], title: data[i].name, capacity: data[i].capacity };
                
                arr.forEach((element, index) => {
                    let posData = { lat: element.latitude, lng: element.longitude };
                    boundaries[i]['points'].push(posData);
                });

            }

            this.boundaries = boundaries;
            
        });

    }

    /**
     * Adjust popup position on polygon clicked
     * @param event 
     * @param polygon 
     */
    polygonClicked(event, polygon) {
        this.defaultOptions.polygonLat = Number(event.latLng.lat());
        this.defaultOptions.polygonLng = Number(event.latLng.lng());
        this.defaultOptions.isPolygonOpen = true;
    }

    /**
     * Update status of isOpen object
     */
    updateState() {
        this.defaultOptions.isPolygonOpen = false;
    }

    /**
     * Search user based on SAP ID
     */
    searchUser(map) {

        if (this.searchKey == '') {
            alert('Please enter SAP ID');
        } else {
            this.dataService.getUserById(this.searchKey)
                                    .subscribe((response) => {
                                        if (response.status == 'Success') {
                                            this.markers = [];
                                            this.markers.push(response.data);
                                            
                                            this.userMap.fitBounds(this.findStoresBounds());
                                            this.defaultOptions.zoom = 16.9;
                                        }
                                    });
        }
    }

    public userMapReady(map){
        this.userMap = map;
    }
    
    public findStoresBounds(){
        let bounds:LatLngBounds = new google.maps.LatLngBounds();
        for(let marker of this.markers){
          bounds.extend(new google.maps.LatLng(marker.latitude, marker.longitude));
        }
        
        return bounds;
    }
    

}
