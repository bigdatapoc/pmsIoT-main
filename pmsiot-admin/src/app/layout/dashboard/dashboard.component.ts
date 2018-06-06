import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';
import { DataService } from '../../shared/services/data.service';
import { GoogleMapsAPIWrapper, MapsAPILoader, AgmMap, LatLngBounds, LatLngBoundsLiteral} from '@agm/core';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

declare var google: any;

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    animations: [routerTransition()]
})
export class DashboardComponent implements OnInit {
    
    closeResult: string;
    defaultOptions = {
        lat: 28.53590728969859,
        lng: 77.3436963558197,
        zoom: 17,
        polygonLat: 0,
        polygonLng: 0,
        isPolygonOpen: false
    };

    messageObj = {
        titleText: null,
        messageText: null
    };
    
    searchKey = null;
    locationHistory = [];
    userMap;
    markerUrl: string = 'assets/images/marker-person.png';
    markerUrlOffline: string = 'assets/images/marker-person.png';
    markers = [];
    boundaries = [];

    constructor(private dataService: DataService, 
                    private loader: MapsAPILoader,
                        private modalService: NgbModal) {
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
        this.locationHistory = [];
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
                                            this.locationHistory = response.data.locationHistory;
                                            this.userMap.fitBounds(this.findStoresBounds());
                                            this.defaultOptions.zoom = 16.9;
                                        }
                                    });
        }
    }

    /**
     * Initialize userMap Object
     * @param map 
     */
    public userMapReady(map){
        this.userMap = map;
    }
    
    /**
     * Adjust map bounds
     */
    public findStoresBounds(){
        let bounds:LatLngBounds = new google.maps.LatLngBounds();
        for(let marker of this.markers){
          bounds.extend(new google.maps.LatLng(marker.latitude, marker.longitude));
        }
        
        return bounds;
    }

    /**
     * Open popup for message/notification
     */
    openMessageModal(content, titleText, type) {
        if (type == 'boundary') {
            this.messageObj.titleText = `Premises: ${titleText}`;
        } else {
            this.messageObj.titleText = `SAP ID: ${titleText}`;
        }
        
        this.modalService.open(content).result.then((result) => {
            this.closeResult = `Closed with: ${result}`;
        }, (reason) => {
            this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
        });
    }

    //TODO: Remove this method and dependencies
    private getDismissReason(reason: any): string {
        if (reason === ModalDismissReasons.ESC) {
            return 'by pressing ESC';
        } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
            return 'by clicking on a backdrop';
        } else {
            return  `with: ${reason}`;
        }
    }
    

}
