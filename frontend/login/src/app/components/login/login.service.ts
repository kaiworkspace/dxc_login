import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  login(username: any, password: any){
    const loginUrl = "http://localhost:8080/auth/login"
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Basic ' + btoa(`${username}:${password}`),
    })
    
    // return this.http.get(loginUrl, {headers , responseType: "text"})
    return this.http.get(loginUrl, {headers, withCredentials: true})
  }
}
