import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class RestrictedService {

  constructor(private http: HttpClient) { }

  fetchData(){
    let url = "http://localhost:8080/dashboard/restricted"
    return this.http.get(url, {withCredentials: true, responseType: "text"})
  }
}
