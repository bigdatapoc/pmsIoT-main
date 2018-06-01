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



}

