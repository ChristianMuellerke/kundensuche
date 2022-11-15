import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs'; 
import { JWTTokenService } from './service/jwttoken.service';
import { LocalStorageService } from './service/localstorage.service';
import { AuthService } from './service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthorizeGuard implements CanActivate {
  constructor(private authService: AuthService,
              private authStorageService: LocalStorageService,
              private jwtService: JWTTokenService,
              private router: Router) {
  }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
      return true;

      // this.authStorageService.get

      // if (this.jwtService.getUser()) {
      //     console.log('Got User!');
      //     if (this.jwtService.isTokenExpired()) {
      //       this.router.navigate(['login']);
      //       // stattdessen das Token refreshen?
      //     } else {
      //       return true;
      //     }
      // } /*else {
      //   return new Promise((resolve) => {
      //     this.authService.signIncallBack().then((e) => {
      //        resolve(true);
      //     }).catch((e) => {
      //       // Should Redirect Sign-In Page
      //     });
      //   });
      // }*/
      // console.log('No User!');
      // return false;
  }

}