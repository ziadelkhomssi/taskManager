import { CanActivateFn, Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication-service';
import { inject, PLATFORM_ID, RESPONSE_INIT } from '@angular/core';
import { map, tap } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';

export const authenticationGuard: CanActivateFn = (route, state) => {
  const authentication = inject(AuthenticationService);
  const platformId = inject(PLATFORM_ID);
  const router = inject(Router);

  if (!isPlatformBrowser(platformId)) {
    return true;
  }

  console.log("checking if logged in!!");
  return authentication.isLoggedIn().pipe(
    map(isLoggedIn =>
      isLoggedIn
        ? true
        : router.createUrlTree(["/authentication/login"])
    )
  );
};

