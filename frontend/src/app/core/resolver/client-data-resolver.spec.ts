import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';

import { clientDataResolver } from './client-data-resolver';
import { ClientDetails } from '../ng-openapi';

describe('clientDataResolver', () => {
  const executeResolver: ResolveFn<ClientDetails> = (...resolverParameters) => 
      TestBed.runInInjectionContext(() => clientDataResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});
