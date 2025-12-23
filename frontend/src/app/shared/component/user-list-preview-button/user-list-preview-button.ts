import { ChangeDetectorRef, Component, EventEmitter, Input, Output } from '@angular/core';
import { UserListModal } from '../user-list-modal/user-list-modal';
import { MatDialog } from '@angular/material/dialog';
import { PageQuery, UserFetcher } from '../../types/types';
import { PageUserSummary, UserSummary } from '../../../core/ng-openapi';
import { FallbackImage } from '../../directive/fallback-image/fallback-image';

@Component({
  selector: 'app-user-list-preview-button',
  imports: [
    FallbackImage
  ],
  templateUrl: './user-list-preview-button.html',
  styleUrl: './user-list-preview-button.css',
})
export class UserListPreviewButton {
  @Input() previewUsers: UserSummary[] = [];
  @Input() totalUsers: number = 0;
  @Input() avatarSize = 40;

  @Output() clicked = new EventEmitter<void>();

  constructor(
    private changeDetectorRef: ChangeDetectorRef,
  ) {}

  onClicked() {
    this.clicked.emit();
  }
}
