import { Injectable } from '@angular/core';
import { ConfirmDialog } from '../../shared/component/confirm-dialog/confirm-dialog';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialog } from '../../shared/component/error-dialog/error-dialog';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class DialogService {

  constructor(
    private dialog: MatDialog,
    private router: Router,
  ) { }

  openErrorDialog(
    message: string, 
    afterClose: (() => void) | null
  ) {
    const dialogRef = this.dialog.open(ErrorDialog, {
      width: "500px",
      data: { message: message },
      panelClass: "error-dialog-panel"
    });
  
    if (afterClose == null) {
      return;
    }
  
    dialogRef.afterClosed().subscribe(() => {
      afterClose();
    });
  }

  openConfirmDialog(
    title: string,
    message: string | null,
    confirmButtonText: string | null,
    cancelButtonText: string | null,
    afterConfirm: () => void
  ) {
    this.dialog.open(ConfirmDialog, {
      width: "500px",
      data: {
        title: title,
        message: message || "This action cannot be undone. Are you sure?",
        confirmButtonText: confirmButtonText || "Delete",
        cancelButtonText: cancelButtonText || "Keep"
      }
    }).afterClosed().subscribe(confirmed => {
      if (!confirmed) {
        return;
      }
      afterConfirm();
    });
  }
}
