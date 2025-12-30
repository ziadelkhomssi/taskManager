import { ChangeDetectorRef, Component, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';
import { MatSliderModule } from '@angular/material/slider';
import { MatSidenav, MatSidenavContainer, MatSidenavContent, MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbar, MatToolbarModule } from '@angular/material/toolbar';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatNavList } from '@angular/material/list';
import { UserDetails, UserSummary } from './core/ng-openapi';
import { UserService } from './core/services/user-service';
import { FallbackImage } from './shared/directive/fallback-image/fallback-image';
import { MatButtonModule, MatIconButton } from '@angular/material/button';
import { NotificationService } from './core/services/notification-service';
import { DialogService } from './core/services/dialog-service';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    RouterLink,
    MatSidenavModule,
    MatSidenavContent, 
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    FallbackImage
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');

  clientUserSummary: UserSummary = {
    id: "def456",
    name: "Jane Developer",
  }
  notificationBellIcon: string = "notification";
  
  constructor(
    private userService: UserService,
    private notificationService: NotificationService,
    private dialogService: DialogService,
    private router: Router,
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
    this.loadClientUserSummary();
  }

  loadClientUserSummary() {
    this.userService.getClientSummary().subscribe({
      next: (response) => {
        this.clientUserSummary = response;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error("Could not load client user!", error);
        this.dialogService.openErrorDialog(
          "Could not load client user! Try again later!",
          null
        )
      }
    });
  }

  checkNotifications() {
    this.notificationService.getHasUnread().subscribe({
      next: (response) => {
        const hasUnread: boolean = response;
        if (hasUnread) {
          this.notificationBellIcon = "notifications_active";
          this.changeDetectorRef.detectChanges();
          return;
        }

        this.notificationBellIcon = "notifications";
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
