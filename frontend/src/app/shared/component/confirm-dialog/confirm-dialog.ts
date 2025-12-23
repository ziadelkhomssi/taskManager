import { Component, Inject } from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA, MatDialogModule} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

export interface ConfirmDialogData {
  title?: string;
  message: string;
  confirmButtonText?: string;
  cancelButtonText?: string;
}

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatIconModule
  ],
  template: `
    <div class="dialog">
        <h2 mat-dialog-title class="confirm-title">
        <mat-icon color="warn" class="confirm-icon">warning_amber</mat-icon>
        {{ data.title || 'Confirm action' }}
        </h2>

        <mat-dialog-content class="confirm-content">
        {{ data.message }}
        </mat-dialog-content>

        <mat-dialog-actions align="end">
        <button mat-button mat-dialog-close>
            {{ data.cancelButtonText || 'Cancel' }}
        </button>

        <button
            mat-flat-button
            color="warn"
            [mat-dialog-close]="true">
            {{ data.confirmButtonText || 'Confirm' }}
        </button>
        </mat-dialog-actions>
    </div>
  `,
  styles: [`
    .dialog {
      padding: 1rem 0.5rem;
    }

    .confirm-title {
      display: flex;
      align-items: center;
    }

    .confirm-icon {
      margin-right: 1rem;
    }

    .confirm-content {
      margin-top: 8px;
      font-size: 14px;
      line-height: 1.5;
    }
  `]
})
export class ConfirmDialog {
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialog>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData
  ) {}
}
