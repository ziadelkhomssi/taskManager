import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { UserService } from '../services/user-service';
import { ClientDetails } from '../ng-openapi';
import { Observable } from 'rxjs';

export const clientDataResolver: ResolveFn<ClientDetails> = (_route, _state) => {
  const userService = inject(UserService);
  return userService.getClientDetails();
};
