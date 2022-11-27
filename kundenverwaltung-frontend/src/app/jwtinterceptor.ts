import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AuthService } from './service/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    
    constructor(private authService: AuthService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this.authService.getJwtToken();
        console.log("req " + request.url + " token=" + token)
        if (!request.url.startsWith("/api/auth")){
            
            if (token) {
                console.log("adding bearer token header...");
                request = request.clone({
                    setHeaders: { Authorization: `Bearer ${token}` }
                });
            }

        }

        return next.handle(request);
    }
}

export const httpInterceptorProviders = [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
];
