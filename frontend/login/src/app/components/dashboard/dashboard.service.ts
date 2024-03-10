import { Injectable } from '@angular/core';
import { HttpClient, HttpHandler, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private http: HttpClient) { }

  loadUserInformation(){
    let url = "http://localhost:8080/dashboard"
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    })
    return this.http.get(url, {headers, withCredentials: true, responseType: "text"})
  }

  logout(){
    let url = "http://localhost:8080/dashboard/logout"
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    })
    return this.http.get(url, {headers, withCredentials: true, responseType: "text"})
  }
}
