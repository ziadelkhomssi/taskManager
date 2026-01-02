import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { ClientDetails, TicketCommentDetails } from '../../../../core/ng-openapi';
import { FallbackImage } from '../../../../shared/directive/fallback-image/fallback-image';
import { MatIconModule } from "@angular/material/icon";
import { MatMenuModule } from '@angular/material/menu';

@Component({
  selector: 'app-ticket-comment',
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    FallbackImage,
    MatIconModule,
    MatMenuModule
],
  templateUrl: './ticket-comment.html',
  styleUrl: './ticket-comment.css',
})
export class TicketComment {
  @Input({ required: true }) comment!: TicketCommentDetails;
  @Input() clientDetails?: ClientDetails;

  @Output() reply = new EventEmitter<TicketCommentDetails>();
  @Output() edit = new EventEmitter<TicketCommentDetails>();
  @Output() delete = new EventEmitter<TicketCommentDetails>();

  onReply(): void {
    console.log(this.comment);
    console.log(this.clientDetails);
    this.reply.emit(this.comment);
  }
}