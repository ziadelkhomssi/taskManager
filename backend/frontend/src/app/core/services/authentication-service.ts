import { HttpClient } from '@angular/common/http';
import { afterNextRender, inject, Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { BaseApiService } from './base-api-service';
import { isPlatformBrowser } from '@angular/common';
import { Router } from 'express';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService extends BaseApiService {
  private readonly authenticationUrl: string = `${this.BASE_URL}/authentication`
  private platformId = inject(PLATFORM_ID);

  isLoggedIn(): Observable<boolean> {
    return this.http.get(`${this.authenticationUrl}/status`, {
      withCredentials: true,
      observe: "response"
    }).pipe(
      map(res => res.status == 200),
      catchError(() => of(false))
    );
  } 
}
