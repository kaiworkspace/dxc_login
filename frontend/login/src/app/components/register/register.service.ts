import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) { }

  register(name: string, username: string, password: string){
    let url = "http://localhost:8080/auth/register"
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    })
    let body = {
      "name": name,
      "username": username,
      "password": password
    }
    // return this.http.post(url, body, {withCredentials: true})
    return this.http.post(url, body, {headers})
  }
}


