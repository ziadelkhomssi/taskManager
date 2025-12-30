import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { TicketCommentCreate, TicketCommentDetails, TicketCommentUpdate, TicketDetails, UserSummary } from '../../../core/ng-openapi';
import { TicketStatusChip } from "../../../shared/component/status-chip/ticket-status-chip/ticket-status-chip";
import { TicketService } from '../../../core/services/ticket-service';
import { DialogService } from '../../../core/services/dialog-service';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { FallbackImage } from '../../../shared/directive/fallback-image/fallback-image';
import { TicketCommentThread } from "../comment/ticket-comment-thread/ticket-comment-thread";
import { TicketCommentService } from '../../../core/services/ticket-comment-service';
import { PageQuery } from '../../../shared/types/types';
import { TicketComment } from '../comment/ticket-comment/ticket-comment';
import { TicketCommentForm } from "../comment/ticket-comment-form/ticket-comment-form";

const DEFAULT_TICKET_COMMENTS_QUERY: PageQuery = {
  page: 0,
  size: 10,
  search: "",
  filter: ""
}

@Component({
  selector: 'app-ticket-page',
  imports: [
    MatCardModule,
    MatButtonModule,
    TicketStatusChip,
    FallbackImage,
    TicketCommentThread,
    TicketCommentForm
],
  templateUrl: './ticket-page.html',
  styleUrl: './ticket-page.css',
})
export class TicketPage {
  @ViewChild(TicketCommentForm) rootCommentForm!: TicketCommentForm;
  ticketDetails: TicketDetails = {
    id: -1,
    title: "PLACEHOLDER",
    description: "PLACEHOLDER",
    userSummary: {
      id: "null",
      name: "PLACEHOLDER",
    },
    priority: "HIGH",
    status: "IN_PROGRESS",
  };

  comments: TicketCommentDetails[] = [];

  constructor(
    private ticketService: TicketService,
    private ticketCommentService: TicketCommentService,
    private dialogService: DialogService,
    private changeDetectorRef: ChangeDetectorRef,
    private location: Location,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      const ticketId: number = params["id"];
      this.loadTicketDetails(ticketId);
      this.loadTicketComments(ticketId);
    });
  }

  loadTicketDetails(id: number) {
    this.ticketService.getDetailsById(id).subscribe({
      next: (response) => {
        this.ticketDetails = response;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error("Could not load ticket!", error);
        this.dialogService.openErrorDialog(
          "Could not load ticket!\nPlease try again later!",
          () => {
            this.location.back();
          }
        );
      },
    });
  }

  loadTicketComments(ticketId: number) {
    this.ticketCommentService
      .getDetailsList(ticketId, DEFAULT_TICKET_COMMENTS_QUERY)
      .subscribe({
        next: (response) => {
          this.comments = response.content || [];
          this.changeDetectorRef.detectChanges();
        },
        error: (error) => {
          console.error("Could not load comments!", error);
          this.dialogService.openErrorDialog(
            "Could not load comments!\nPlease try again later!",
            null
          );
        },
      });
  }
  
  onCreateComment(command: TicketCommentCreate) {
    this.ticketCommentService.create(command).subscribe({
      next: () => {
        if (command.parentCommentId == null) {
          this.rootCommentForm.resetForm();
        }
        this.loadTicketComments(this.ticketDetails.id);
      },
      error: (error) => {
        console.error("Could not create comment!", error);
        this.dialogService.openErrorDialog(
          "Could not create comment! Please try again!",
          null
        );
      },
    });
  }

  onUpdateComment(command: TicketCommentUpdate) {
    this.ticketCommentService.update(command).subscribe({
      next: () => {
        this.loadTicketComments(this.ticketDetails.id);
      },
      error: (error) => {
        console.error("Could not update comment!", error);
        this.dialogService.openErrorDialog(
          "Could not update comment! Please try again!",
          null
        );
      },
    });
  }

  onDeleteComment(comment: TicketCommentDetails) {
    this.dialogService.openConfirmDialog(
      "Delete Comment?", null, null, null,
      () => {
        this.ticketCommentService.deleteById(comment.id).subscribe({
          next: () => {
            this.loadTicketComments(this.ticketDetails.id);
          },
          error: (error) => {
            console.error("Could not delete comment!", error);
            this.dialogService.openErrorDialog(
              "Could not delete comment! Please try again!",
              null
            );
          },
    });});
  }

  onUserClick() {
    this.router.navigate([`/user/${this.ticketDetails.userSummary.id}`]);
  }

  updateTicket() {
    this.router.navigate([`/ticket/update/${this.ticketDetails.id}`]);
  }

  deleteTicket() {
    this.dialogService.openConfirmDialog(
      "Delete Current Ticket?",
      null,
      null,
      null,
      () => {
        this.ticketService.deleteById(this.ticketDetails.id).subscribe({
          next: () => this.location.back(),
          error: () =>
            this.dialogService.openErrorDialog(
              "Could not delete ticket!\nPlease try again later!",
              null
            ),
        });
      }
    );
  }
}

