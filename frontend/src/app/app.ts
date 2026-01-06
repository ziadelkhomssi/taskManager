import { ChangeDetectorRef, Component, signal } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';
import { MatSliderModule } from '@angular/material/slider';
import { MatSidenav, MatSidenavContainer, MatSidenavContent, MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbar, MatToolbarModule } from '@angular/material/toolbar';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatNavList } from '@angular/material/list';
import { ClientDetails, UserDetails, UserSummary } from './core/ng-openapi';
import { UserService } from './core/services/user-service';
import { FallbackImage } from './shared/directive/fallback-image/fallback-image';
import { MatButtonModule, MatIconButton } from '@angular/material/button';
import { NotificationService } from './core/services/notification-service';
import { DialogService } from './core/services/dialog-service';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { MatBadgeModule } from '@angular/material/badge';

@Component({
  selector: 'app-root',
  imports: [
    AsyncPipe,
    RouterOutlet,
    RouterLink,
    MatSidenavModule,
    MatSidenavContent, 
    MatToolbarModule,
    MatIconModule,
    MatBadgeModule,
    MatButtonModule,
    FallbackImage
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');

  clientDetails$!: Observable<ClientDetails | null>;
  hasUnreadNotifications: boolean = false;
  
  constructor(
    private userService: UserService,
    private notificationService: NotificationService,
    private dialogService: DialogService,
    private router: Router,
    private route: ActivatedRoute,
    private changeDetectorRef: ChangeDetectorRef
  ) {
    this.router.events
      .subscribe((event) => {
        if (!(event instanceof NavigationEnd)) {
          return;
        }

        this.checkNotifications();
      }
    );
  }

  ngOnInit() {
    this.clientDetails$ = this.userService.clientDetails$
  }

  checkNotifications() {
    this.notificationService.getHasUnread().subscribe({
      next: (response) => {
        this.hasUnreadNotifications = response;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error("Could not load notifications!", error);
        this.dialogService.openErrorDialog(
          "Could not load notifications! Try again later!",
          null
        )
      }
    });
  }

  logout() {
    console.log("woah!! you logged out (jk)")
  }
}
