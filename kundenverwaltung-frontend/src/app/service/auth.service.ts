import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { lastValueFrom, Observable, take } from 'rxjs';
import { JwtData, LoginData } from '../authorization';
import { LocalStorageService } from './localstorage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loginEndpointUrl: string;
  private localStorageService: LocalStorageService;
  
  constructor(private http: HttpClient, private storageService: LocalStorageService) {
    this.loginEndpointUrl = 'http://localhost:8080/api/auth/signin';
    this.localStorageService = storageService;
  }

  async login(username: string, password: string, tenantId: number) {
    const loginData : LoginData = new LoginData;
    loginData.password = password;
    loginData.tenantId = tenantId;
    loginData.username = username;

    console.log("starting login req")
    await lastValueFrom(this.http.post<JwtData>(this.loginEndpointUrl, loginData))
      .then((jwt:JwtData) => this.setSession(jwt))
      .catch((e) => console.log(`An error occurred when getting the last element: ${e}`))
      ;
    console.log("ending login req")
  }

  private setSession(jwtData: JwtData) {
    console.log('JwtData from Auth-Backend: ' + JSON.stringify(jwtData));
    if (jwtData.token) {
      const token: string = jwtData.token;
      console.log('Token from JwtData', token);
      this.localStorageService.set('AuthKey', token);
    }
  }     

  public getJwtToken(): string {
    const token : any = this.localStorageService.get('AuthKey');
    return token;
  }

  public deleteJwtToken(): void {
    console.log("removing token from localstorage");
    this.localStorageService.remove('AuthKey');
  }
}
