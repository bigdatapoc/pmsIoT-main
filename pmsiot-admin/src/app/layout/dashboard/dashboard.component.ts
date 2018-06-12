import { Component, OnInit } from '@angular/core';
import { routerTransition } from '../../router.animations';
import { DataService } from '../../shared/services/data.service';
import { GoogleMapsAPIWrapper, MapsAPILoader, AgmMap, LatLngBounds, LatLngBoundsLiteral} from '@agm/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NotificationsService } from 'angular2-notifications';

import { Paho } from 'ng2-mqtt/mqttws31';

declare var google: any;

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    animations: [routerTransition()]
})
export class DashboardComponent implements OnInit {
    
    private _client: Paho.MQTT.Client;
    modalRef: NgbModalRef;
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
        id: null,
        type: null,
        titleText: null,
        messageText: null
    };
    
    adminNotifObj = {
        message: null,
        showNotif: false
    };

    trackingObj = {
        userId: null,
        index: null,
        isLiveTrackerOn: false,
    };
    successMsg = null;
    showSending = false;
    searchKey = null;
    locationHistory = [];
    userMap;
    markerUrl: string = 'assets/images/marker-person.png';
    markerUrlOffline: string = 'assets/images/marker-person.png';
    markers = [];
    boundaries = [];
    heatmap = null;
    showHeatMap = false;
    currentCount = null;
    infoWindow;

    constructor(private dataService: DataService, 
                    private loader: MapsAPILoader,
                    private modalService: NgbModal,
                    private _notifications: NotificationsService) {

        this._client = new Paho.MQTT.Client("test.mosquitto.org", 8080, "", "client_id1111111");

        this._client.onConnectionLost = (responseObject: Object) => {
            console.log('Connection lost.');
        };

        this._client.onMessageArrived = (message: Paho.MQTT.Message) => {
            console.log('Message arrived.');
            let msg = JSON.parse(message.payloadString);
            
			if (msg.to.indexOf('user') > -1) {
				this.updateUserLocation(msg.data);
			} else {
				this.showAdminNotification(msg);
			}
            

        };
        
        this._client.connect({ onSuccess: () => {
            console.log('Connection established.');
			this._client.subscribe('admin/notification', {}); 
        }});
    }

    ngOnInit() {

        setInterval(() => {
            this.getAllUsers();
        }, 60000);
        
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
                
                boundaries[i] = { 
                            id: data[i].id,
                            points: [], 
                            title: data[i].name, 
                            capacity: data[i].capacity,
                            master: data[i].master
                        };
                
                arr.forEach((element, index) => {
                    let posData = { lat: element.latitude, lng: element.longitude };
                    boundaries[i]['points'].push(posData);
                });

            }

            this.boundaries = boundaries;

            this.heatmap = new google.maps.visualization.HeatmapLayer({
                data: this.getHeatmapPoints(),
                map: this.userMap
            });
            
        });

    }


    /**
     * prepare heat map points
     */
    getHeatmapPoints() {
        let arr = [];
        
        for (var i = 0; i < this.markers.length; i++) {
            arr.push(new google.maps.LatLng(this.markers[i].latitude, this.markers[i].longitude))
        }

        return arr;
    }


    toggleHeatmap() {

        this.showHeatMap = !this.showHeatMap;

        if (this.showHeatMap) {
            this.heatmap.setMap(this.userMap);
        } else {
            this.heatmap.setMap(null);
        }
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
    openMessageModal(content, titleText, type, id) {
        if (type == 'boundary') {
            this.messageObj.titleText = `Premises: ${titleText}`;
        } else {
            this.messageObj.titleText = `SAP ID: ${titleText}`;
        }

        this.messageObj.type = type;
        this.messageObj.id = id;
        
        this.modalRef = this.modalService.open(content);
    }

    /**
     * Send notification message
     */
    sendMessage() {

        this.showSending = true;
        this.dataService.sendNotification(this.messageObj)
                                    .subscribe((response) => {
                                        if (response.status == 'Success') {
                                            this.messageObj = {
                                                id: null,
                                                type: null,
                                                titleText: null,
                                                messageText: null
                                            };

                                            this.showSending = false;
                                            this.successMsg = 'Notification sent successfully.';

                                            setTimeout(() => {
                                                this.successMsg = null;
                                            }, 3000);
                                        }
                                    });
        
    }

    /**
     * Start live user location tracking
     * @param content 
     * @param userId 
     * @param index 
     */
    trackUser(content, userId, index) {
        this._client.subscribe('user/'+ userId + '/tracking', {});   
        this.trackingObj.userId = userId;
		this.trackingObj.index = index;
        this.trackingObj.isLiveTrackerOn = true;		
    }

    /**
     * Stop user tracking
     */
    stopTracking() {
        this._client.unsubscribe('user/' + this.trackingObj.userId + '/tracking', {});
        this.trackingObj.isLiveTrackerOn = false;    
    }


    /**
     * Update user location in map
     * @param data 
     */
    updateUserLocation(data) {	
        this.markers[this.trackingObj.index].latitude = Number(data.latitude);
        this.markers[this.trackingObj.index].longitude = Number(data.longitude);
    }
    

    /**
     * show admin notification
     * @param data 
     */
    showAdminNotification(data) {
        
        this._notifications.create(
            '',
            data.body,
            'info',
            {
                timeOut: 4000
            }
        )
    }


    /**
     * Close admin notification
     */
    closeNotif() {
        this.adminNotifObj.showNotif = false;  
    }
    
    /**
     * get current count of location
     * @param id location id
     */
	getCurrentCount(id) {
		this.dataService.getLocationCount(id)
                            .subscribe((response) => {
                                if (response.status == 'Success') {
                                    this.currentCount = response.data.count;
                                }
                            });
    }
    

    openIW(data) {
        if(this.infoWindow){
            this.infoWindow.close();
        }

        this.infoWindow = data;
    }

}
