import { ChangeDetectorRef, Component, signal } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { MatSliderModule } from '@angular/material/slider';
import { MatSidenav, MatSidenavContainer, MatSidenavContent, MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbar } from '@angular/material/toolbar';
import { MatIcon } from '@angular/material/icon';
import { MatNavList } from '@angular/material/list';
import { UserDetails, UserSummary } from './core/ng-openapi';
import { UserService } from './core/services/user-service';
import { FallbackImage } from './shared/directive/fallback-image/fallback-image';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    RouterLink, 
    MatSidenavContainer, 
    MatSidenav, 
    MatSidenavModule,
    MatSidenavContent, 
    MatToolbar,
    MatIcon,
    FallbackImage
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
  
  constructor(
    private userService: UserService,
    private router: Router,
    private changeDetectorRef: ChangeDetectorRef
  ) {}

  clientUserSummary: UserSummary = {
    id: "def456",
    name: "Jane Developer",
  }

  ngOnInit() {
    this.loadClientUserSummary();
  }

  loadClientUserSummary() {
    this.userService.getClientSummary().subscribe({
      next: (response) => {
        this.clientUserSummary = response;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error('Could not load client user!', error);
      }
    });
  }

  logout() {
    console.log("woah!! you logged out (jk)")
  }
}
