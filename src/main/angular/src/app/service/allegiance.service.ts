import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {Allegiance} from '../model/allegiance';

@Injectable({
  providedIn: 'root'
})
export class AllegianceService {

  constructor(private http: HttpClient) {
  }

  public getAll(): Observable<Allegiance[]> {
    return this.http.get<Allegiance[]>(environment.apiUrl + '/api/allegiances');
  }
}
