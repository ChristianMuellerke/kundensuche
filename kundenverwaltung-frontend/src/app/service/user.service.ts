import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JwtData, LoginData } from '../authorization';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private loginEndpointUrl: string;

    constructor(private http: HttpClient) {
    this.loginEndpointUrl = 'http://localhost:8080/api/auth/signin';
   }

   public login(username: string, password: string, tenantId: number): Observable<JwtData> {
    const loginData : LoginData = new LoginData;
    loginData.password = password;
    loginData.tenantId = tenantId;
    loginData.username = username;

    return this.http.post<JwtData>(this.loginEndpointUrl, loginData);
  }

}
