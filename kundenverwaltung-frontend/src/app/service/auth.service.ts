import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
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

  public login(username: string, password: string, tenantId: number) {
    const loginData : LoginData = new LoginData;
    loginData.password = password;
    loginData.tenantId = tenantId;
    loginData.username = username;

    this.http.post<JwtData>(this.loginEndpointUrl, loginData).subscribe((jwtData) => this.setSession(jwtData), () => console.log('auth failed'));
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

}
