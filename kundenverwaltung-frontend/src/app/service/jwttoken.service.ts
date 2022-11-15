import { Injectable } from '@angular/core';
import jwt_decode from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class JWTTokenService {

    jwtToken: string | undefined;
    decodedToken: { [key: string]: string } | undefined;

    constructor() {
    }

    setToken(token: string) {
      if (token) {
        this.jwtToken = token;
      }
    }

    decodeToken() {
      if (this.jwtToken) {
        this.decodedToken = jwt_decode(this.jwtToken);
        console.log('Decoded Token: ' + this.decodedToken)
      }
    }

    getDecodeToken() {
      if (this.jwtToken) {
        return jwt_decode(this.jwtToken);
      }
      return undefined;
    }

    getUser() {
      this.decodeToken();
      return this.decodedToken ? this.decodedToken['username'] : null;
    }

    getExpiryTime() {
      this.decodeToken();
      return this.decodedToken ? this.decodedToken['exp'] : null;
    }

    isTokenExpired(): boolean {
      const expiryTime: number = Number(this.getExpiryTime());
      if (expiryTime) {
        return ((1000 * expiryTime) - (new Date()).getTime()) < 5000;
      } else {
        return false;
      }
    }
}
