import { Component, inject, PLATFORM_ID } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-authenticate',
  imports: [],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private platformId = inject(PLATFORM_ID);

  ngOnInit() {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    window.location.href = `${environment.API_URL}/oauth2/authorization/azure`;
  }
}
