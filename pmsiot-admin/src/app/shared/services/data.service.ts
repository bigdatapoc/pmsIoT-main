import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class DataService {

    constructor(private http: HttpClient) { }

    domain = environment.apiPath;

    /**
     * method to get all users location data from REST Api
     */
    getAllUsers(): Observable<any>{

        return this.http.get( this.domain + '/user')
                        .pipe(
                            catchError((error: any) => Observable.throw(error.json().error || 'server error'))
                        );
    }
    
    /**
     * method to get all geofence data
     */
    getGeoFenceLocations(): Observable<any>{

        return this.http.get( this.domain + '/location')
                        .pipe(
                            catchError((error: any) => Observable.throw(error.json().error || 'server error'))
                        );
    }

    /**
     * method to get all users location data from REST Api
     */
    getUserById(sapId): Observable<any>{

        return this.http.get( this.domain + '/userDetails')
                        .pipe(
                            catchError((error: any) => Observable.throw(error.json().error || 'server error'))
                        );
    }

    /**
     * send notification to users
     * @param messageObj 
     */
    sendNotification(messageObj): Observable<any>{
        let obj = {
            from: 'admin',
            to: messageObj.id,
            title: messageObj.titleText,
            body: messageObj.messageText
        };

        let url = '';

        if (messageObj.type == 'individual') {
            url = this.domain + '/user/' + obj.to + '/notify';
        } else {
            url = this.domain + '/location/' + obj.to + '/notify';
        }

        return this.http.post( url, obj)
                        .pipe(
                            catchError((error: any) => Observable.throw(error.json().error || 'server error'))
                        );
    }


}

