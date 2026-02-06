import { inject, PLATFORM_ID } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { UserService } from '../services/user-service';
import { ClientDetails } from '../ng-openapi';
import { Observable, of } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';

export const clientDataResolver: ResolveFn<ClientDetails> = (_route, _state) => {
  const platformId = inject(PLATFORM_ID);
  if (!isPlatformBrowser(platformId)) {
    return of({
      userId: "PLACEHOLDER",
      name: "PLACEHOLDER",
      permissions: {
        canManipulateProject: false,
        canManipulateSprint: false,
        canManipulateTicket: false,
      }
    });
  }
  
  const userService = inject(UserService);
  return userService.getClientDetails();
};
