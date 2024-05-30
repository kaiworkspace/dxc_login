import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  login(username: any, password: any){
    const loginUrl = "http://localhost:8080/auth/login"
    let encodedCredentials = this.utf8ToBase64(`${username}:${password}`)

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Credentials': "true",
      // Authorization: 'Basic ' + btoa(`${username}:${password}`),
      Authorization: 'Basic ' + encodedCredentials,
    })

    return this.http.get(loginUrl, {headers, withCredentials: true})
  }

  utf8ToBase64(str: string){
    let utf8Bytes = new TextEncoder().encode(str)
    let binaryString = ""
    for (let i=0; i<utf8Bytes.length; i++){
      binaryString += String.fromCharCode(utf8Bytes[i])
    }
    return btoa(binaryString)
  }

  // for testing
  base64ToUtf8(str: string){
    let binaryString = atob(str)
    let utf8Bytes = new Uint8Array(binaryString.length);
    for (let i = 0; i < binaryString.length; i++) {
        utf8Bytes[i] = binaryString.charCodeAt(i);
    }
    return new TextDecoder().decode(utf8Bytes);
  }
}
