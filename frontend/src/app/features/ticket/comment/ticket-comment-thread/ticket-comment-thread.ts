import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TicketComment } from "../ticket-comment/ticket-comment";
import { TicketCommentCreate, TicketCommentDetails, TicketCommentUpdate, UserSummary } from '../../../../core/ng-openapi';
import { TicketCommentForm } from "../ticket-comment-form/ticket-comment-form";

@Component({
  selector: 'app-ticket-comment-thread',
  imports: [TicketComment, TicketCommentForm],
  templateUrl: './ticket-comment-thread.html',
  styleUrl: './ticket-comment-thread.css',
})
export class TicketCommentThread {
  @Input() comments: TicketCommentDetails[] = [];
  @Input() parentCommentId?: number;
  @Input({ required: true }) ticketId!: number;

  @Output() createComment = new EventEmitter<TicketCommentCreate>();
  @Output() updateComment = new EventEmitter<TicketCommentUpdate>();
  @Output() deleteComment = new EventEmitter<TicketCommentDetails>();

  editingCommentId?: number;
  replyingToCommentId?: number;

  get childComments(): TicketCommentDetails[] {
    return this.comments.filter(
      child => child.parentCommentId == this.parentCommentId
    );
  }

  startEdit(comment: TicketCommentDetails) {
    this.editingCommentId = comment.id;
    this.replyingToCommentId = undefined;
  }

  startReply(comment: TicketCommentDetails) {
    this.replyingToCommentId = comment.id;
    this.editingCommentId = undefined;
  }

  cancel() {
    this.editingCommentId = undefined;
    this.replyingToCommentId = undefined;
  }

  onCreate(command: TicketCommentCreate) {
    this.createComment.emit(command);
    this.cancel();
  }

  onUpdate(command: TicketCommentUpdate) {
    this.updateComment.emit(command);
    this.cancel();
  }

  onDelete(comment: TicketCommentDetails) {
    this.deleteComment.emit(comment);
  }
}