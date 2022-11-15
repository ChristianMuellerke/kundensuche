import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Tenant } from '../tenant';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TenantService {

  private tenantsUrl: string;

  constructor(private http: HttpClient) {
    this.tenantsUrl = 'http://localhost:8080/tenants/';
   }

  public findAll(): Observable<Tenant[]> {
    return this.http.get<Tenant[]>(this.tenantsUrl + "all");
  }

  public getTenant(id: Number): Observable<Tenant> {
    return this.http.get<Tenant>(this.tenantsUrl + id);
  }
}
