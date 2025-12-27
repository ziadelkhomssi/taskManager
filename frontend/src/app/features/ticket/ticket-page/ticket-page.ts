import { ChangeDetectorRef, Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { TicketDetails } from '../../../core/ng-openapi';
import { TicketStatusChip } from "../../../shared/component/status-chip/ticket-status-chip/ticket-status-chip";
import { TicketService } from '../../../core/services/ticket-service';
import { DialogService } from '../../../core/services/dialog-service';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { FallbackImage } from '../../../shared/directive/fallback-image/fallback-image';

@Component({
  selector: 'app-ticket-page',
  imports: [
    MatCardModule,
    MatButtonModule,
    TicketStatusChip,
    FallbackImage
  ],
  templateUrl: './ticket-page.html',
  styleUrl: './ticket-page.css',
})
export class TicketPage {
  ticketDetails: TicketDetails = {
    id: -1,
    title: "PLACEHOLDER",
    description: "PLACEHOLDER",
    userSummary: {
      id: "null",
      name: "PLACEHOLDER"
    },
    priority: "HIGH",
    status: "IN_PROGRESS"
  };

  constructor(
    private ticketService: TicketService, 
    private dialogService: DialogService,
    private changeDetectorRef: ChangeDetectorRef,
    private location: Location,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.loadTicketDetails(params["id"])
    });
  }

  loadTicketDetails(id: number) {
    this.ticketService.getDetailsById(id).subscribe({
      next: (response) => {
        this.ticketDetails = response;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error('Could not load ticket!', error);
        this.dialogService.openErrorDialog(
          "Could not load ticket!\nPlease try again later!", 
          () => {this.location.back();}
        );
      }
    });
  }

  onUserClick() {
    this.router.navigate([`/user/${this.ticketDetails.userSummary.id}`]);
  }

  updateTicket() {
    this.router.navigate([`/ticket/update/${this.ticketDetails.id}`])
  }
  deleteTicket() {
    this.dialogService.openConfirmDialog(
      "Delete Current Ticket?", null, null, null,
      () => {
        this.ticketService.deleteById(
          this.ticketDetails.id
        ).subscribe({
          next: () => {
            this.location.back();
          },
          error: (error) => {
            console.error("Could not delete ticket!", error);
            this.dialogService.openErrorDialog(
              "Could not delete ticket!\nPlease try again later!", null
            );
    }});});
  }

  createComment() {
    console.log("creating comment!!");
  }
  updateComment() {
    console.log("updating comment!!");
  }
  deleteComment() {
    console.log("deleting comment!!");
  }
}
