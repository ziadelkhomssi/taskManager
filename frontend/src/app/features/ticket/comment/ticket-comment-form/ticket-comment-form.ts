import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { ClientDetails, TicketCommentCreate, TicketCommentDetails, TicketCommentUpdate } from '../../../../core/ng-openapi';
import { MatFormFieldModule } from "@angular/material/form-field";
import { FallbackImage } from '../../../../shared/directive/fallback-image/fallback-image';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-ticket-comment-form',
  imports: [
    MatCardModule,
    MatButtonModule,
    MatInputModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    FallbackImage
],
  templateUrl: './ticket-comment-form.html',
  styleUrl: './ticket-comment-form.css',
})
export class TicketCommentForm {
  @Input() comment?: TicketCommentDetails;
  @Input() clientDetails?: ClientDetails;
  @Input() parentCommentId?: number;
  @Input() isUpdate = false;
  @Input() ticketId!: number;
  @Input() hasCancel: boolean = true;
  @Input() resetOnSuccess = false;
  @Input() submitLabel = "Comment";

  @Output() create = new EventEmitter<TicketCommentCreate>();
  @Output() update = new EventEmitter<TicketCommentUpdate>();
  @Output() cancel = new EventEmitter<void>();

  commentControl = new FormControl("", {
    nonNullable: true,
    validators: [Validators.required],
  });

  ngOnInit(): void {
    if (this.isUpdate && this.comment) {
      this.commentControl.setValue(this.comment.comment);
    }
  }

  resetForm() {
    this.commentControl.reset();
  }

  onSubmit(): void {
    if (this.isUpdate) {
      if (!this.comment) {
        throw new Error("Update requires a comment!");
      }

      const updateCommand: TicketCommentUpdate = {
        id: this.comment.id,
        comment: this.commentControl.value,
      };

      this.update.emit(updateCommand);
      return;
    }

    const createCommand: TicketCommentCreate = {
      comment: this.commentControl.value,
      ticketId: this.ticketId,
      parentCommentId: this.parentCommentId,
    };

    this.create.emit(createCommand);
  }
}
