import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { Location } from '@angular/common';
import { UserDetails } from '../../../core/ng-openapi';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../../core/services/user-service';
import { FallbackImage } from '../../../shared/directive/fallback-image/fallback-image';

@Component({
  selector: 'app-user-profile',
  imports: [FallbackImage],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css',
})
export class UserProfile {
  @Input() userDetails: UserDetails = {
    id: "NULL",
    name: "PLACEHOLDER",
    job: "PLACEHOLDER",
  };

  constructor(
    private userService: UserService,
    private location: Location,
    private changeDetectorRef: ChangeDetectorRef,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.loadUserDetails(params["id"])
    });
  }

  loadUserDetails(id: string) {
    this.userService.getDetailsById(id).subscribe({
      next: (response) => {
        this.userDetails = response;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error('Could not load user!', error);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
}
