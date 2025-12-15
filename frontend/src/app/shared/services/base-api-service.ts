import { HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';

export abstract class BaseApiService {
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

