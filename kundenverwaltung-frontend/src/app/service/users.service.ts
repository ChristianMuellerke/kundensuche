import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../user';
import { LocalStorageService } from './localstorage.service';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private usersUrl: string;
  private localStorageService: LocalStorageService;

  constructor(private http: HttpClient, private storageService: LocalStorageService) {
    this.usersUrl = 'http://localhost:8080/users/';
    this.localStorageService = storageService;
   }

  public findAll(): Observable<User[]> {
    //let headers = new HttpHeaders();
    //let authKey = this.localStorageService.get('AuthKey');
    //headers = headers.set('Authorization', 'authKey');

    return this.http.get<User[]>(this.usersUrl + "all");
  }

}
