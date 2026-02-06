import { isPlatformBrowser } from '@angular/common';
import { Component, inject, PLATFORM_ID } from '@angular/core';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-logout',
  imports: [],
  templateUrl: './logout.html',
  styleUrl: './logout.css',
})
export class Logout {
  private platformId = inject(PLATFORM_ID);
  
  ngOnInit() {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    window.location.href = `${environment.API_URL}/authentication/logout`;
  }
}
