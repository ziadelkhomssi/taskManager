import { ChangeDetectorRef, Component } from '@angular/core';
import { SprintDetails, TicketDetails, TicketSummary, UserSummary } from '../../../core/ng-openapi';
import { EntityTable, TableColumn } from '../../../shared/component/entity-table/entity-table';
import { TicketService } from '../../../core/services/ticket-service';
import { ActivatedRoute, Router } from '@angular/router';
import { SprintService } from '../../../core/services/sprint-service';
import { Location, DatePipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { PageQuery } from '../../../shared/types/types';
import { UserListPreviewButton } from "../../../shared/component/user-list-preview-button/user-list-preview-button";
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { UserListModal } from '../../../shared/component/user-list-modal/user-list-modal';
import { ErrorDialog } from '../../../shared/component/error-dialog/error-dialog';
import { DialogService } from '../../../core/services/dialog-service';


const DEFAULT_PREVIEW_PARTICIPANTS_QUERY: PageQuery = {
  page: 0,
  size: 3,
  search: "",
  filter: ""
}
const DEFAULT_TICKETS_QUERY: PageQuery = {
  page: 0,
  size: 10,
  search: "",
  filter: ""
}

@Component({
  selector: 'app-sprint-page',
  imports: [
    MatCardModule,
    MatButtonModule,
    EntityTable,
    DatePipe,
    UserListPreviewButton
],
  templateUrl: './sprint-page.html',
  styleUrl: './sprint-page.css',
})
export class SprintPage {
  sprintDetails: SprintDetails = {
    id: -1,
    title: "PLACEHOLDER",
    description: "PLACEHOLDER",
    startDate: new Date(),
    dueDate: new Date(),
    status: "PLANNED",
  };

  previewParticipants: UserSummary[] = [];

  tickets: TicketSummary[] = [];

  columns: TableColumn<TicketSummary>[] = [
    { columnDef: 'id', header: 'ID', cell: ticket => ticket.id.toString() },
    { columnDef: 'title', header: 'Title', cell: ticket => ticket.title },
    { columnDef: 'status', header: 'Status', cell: ticket => ticket.status }
  ];

  actions = [
    {
      label: 'Update',
      callback: (ticket: TicketSummary) => this.updateTicket(ticket)
    },
    {
      label: 'Delete',
      callback: (ticket: TicketSummary) => this.deleteTicket(ticket)
    },
  ];

  filters: string[] = [
    "Ticket",
    "Status",
    "User"
  ];

  lastTicketsQuery: PageQuery = DEFAULT_TICKETS_QUERY;
  pageIndex = 0;
  pageSize = 10;
  totalElements = 0;

  sprintUserFetcher = (query: PageQuery) => 
    this.sprintService.getUserSummaryList(this.sprintDetails.id, query);

  constructor(
    private sprintService: SprintService, 
    private ticketService: TicketService, 
    private dialogService: DialogService,
    private changeDetectorRef: ChangeDetectorRef,
    private dialog: MatDialog,
    private location: Location,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.loadSprintDetails(params["id"])
    });
  }

  loadSprintDetails(id: number) {
    this.sprintService.getDetailsById(id).subscribe({
      next: (response) => {
        this.sprintDetails = response;
        this.loadPreviewParticipants();
        this.loadTickets(DEFAULT_TICKETS_QUERY);

        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error('Could not load sprint!', error);
        this.dialogService.openErrorDialog(
          "Could not load sprint!\nPlease try again later!", 
          () => {this.router.navigate(["/"])}
        );
      }
    });
  }

  loadPreviewParticipants() {
    this.sprintUserFetcher(DEFAULT_PREVIEW_PARTICIPANTS_QUERY).subscribe({
      next: (response) => {
        this.previewParticipants = response.content || [];
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error('Could not load sprint participants!', error);
        this.dialogService.openErrorDialog(
          "Could not load sprint participants!\nPlease try again later!", 
          null
        );
      }
    });
  }

  loadTickets(pageQuery: PageQuery) {
    this.sprintService.getTicketSummaryList(
      this.sprintDetails.id,
      pageQuery
    ).subscribe({
      next: (response) => {
        this.tickets = response.content || [];
        this.pageIndex = response.pageable?.pageNumber || 0;
        this.pageSize = response.pageable?.pageSize || 0;
        this.totalElements = response.totalElements || 0;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error('Could not load tickets!', error);
        this.tickets = [];
        this.pageIndex = 0;
        this.pageSize = 0;
        this.totalElements = 0;
        this.changeDetectorRef.detectChanges();

        this.dialogService.openErrorDialog(
          "Could not load tickets!\nPlease try again later!", 
          null
        );
      }
    })
  }

  updateSprint() {
    this.router.navigate(["/sprint/update/" + this.sprintDetails.id]);
  }
  deleteSprint() {
    this.dialogService.openConfirmDialog(
      "Delete Current Sprint?", null, null, null,
      () => {
        this.sprintService.deleteById(
          this.sprintDetails.id
        ).subscribe({
          next: () => {
            this.location.back(); // i'd prefer a more specific back
          },
          error: (error) => {
            console.error("Could not delete project!", error);
            this.dialogService.openErrorDialog("Could not delete project!\nPlease try again later!", null);
          }
    });});
  }

  onPageQuery(pageQuery: PageQuery) {
    this.lastTicketsQuery = pageQuery;
    this.loadTickets(pageQuery);
  }

  onRowClick(ticket: TicketSummary) {
    this.viewTicket(ticket);
  }

  onOpenParticipantList() {
    this.dialog.open(UserListModal, {
      maxWidth: "600px",
      width: '600px',
      data: {
        fetchUsers: this.sprintUserFetcher,
        onUserClick: (dialogRef: MatDialogRef<UserListModal>, user: UserSummary) => {
          dialogRef.close();
          this.router.navigate(["/user/" + user.id]);
        },
        description: "Sprint Participants"
      }
    });
  }

  viewTicket(ticket: TicketSummary) {
    this.router.navigate(["/ticket/" + ticket.id]);
  }
  createTicket() {
    this.router.navigate(["/ticket/create"]);
  }
  updateTicket(ticket: TicketSummary) {
    this.router.navigate(["/ticket/update/" + ticket.id]);
  }
  deleteTicket(ticket: TicketSummary) {
    this.dialogService.openConfirmDialog(
      `Delete Ticket ${ticket.title}?`, null, null, null,
      () => {
        this.ticketService.deleteById(
          ticket.id
        ).subscribe({
          next: () => {
            this.loadTickets(this.lastTicketsQuery);
          },
          error: (error) => {
            console.error("Could not delete sprint!", error);
            this.dialogService.openErrorDialog(
              "Could not delete sprint!\nPlease try again later!", null
            );
    }});});
  }
}
