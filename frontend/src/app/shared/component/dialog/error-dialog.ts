import { Component, Inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';

export interface ErrorDialogData {
  title?: string;
  message: string;
}

@Component({
  selector: 'app-error-dialog',
  standalone: true,
  imports: [
    MatDialogModule, 
    MatButtonModule, 
    MatIconModule
  ],
  template: `
    <div class="dialog">
      <h2 mat-dialog-title class="error-title">
        <mat-icon color="warn" class="error-icon">error_outline</mat-icon>
        {{ data.title || 'Something went wrong' }}
      </h2>

      <mat-dialog-content class="error-content">
        {{ data.message }}
      </mat-dialog-content>

      <mat-dialog-actions align="end">
        <button mat-flat-button color="warn" mat-dialog-close>
          Close
        </button>
      </mat-dialog-actions>
    </div>
  `,
  styles: [`
    .dialog {
      padding: 1rem 0.5rem;
    }
    
    .error-title {
      display: flex;
      align-items: center;
    }

    .error-icon {
      margin-right: 1rem;
    }

    .error-content {
      font-size: 14px;
      line-height: 1.5;
    }
  `]
})
export class ErrorDialog {
  constructor(@Inject(MAT_DIALOG_DATA) public data: ErrorDialogData) {}
}
