import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export abstract class BaseApiService {
  protected BASE_URL: string = environment.API_URL;

  constructor(protected http: HttpClient) { }

  protected handleError(error: HttpErrorResponse) {
    const message =
      error.error?.message ??
      'Something went wrong. Please try again later.';

    console.error('API Error:', error);

    return throwError(() => ({
      message,
      status: error.status
    }));
  }
}

