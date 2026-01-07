import { ChangeDetectorRef, Component, TemplateRef, ViewChild } from '@angular/core';
import { ClientDetails, SprintDetails, TicketDetails, TicketSummary, UserSummary } from '../../../core/ng-openapi';
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
import { ErrorDialog } from '../../../shared/component/dialog/error-dialog';
import { DialogService } from '../../../core/services/dialog-service';
import { SprintStatusChip } from "../../../shared/component/status-chip/sprint-status-chip/sprint-status-chip";
import { TicketStatusChip } from "../../../shared/component/status-chip/ticket-status-chip/ticket-status-chip";
import { CrudTable } from '../../../shared/component/crud-table/crud-table';
import { EntityCard } from "../../../shared/component/entity-card/entity-card";

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

export interface TicketStatusCellContext {
  $implicit: TicketDetails["status"];
}

@Component({
  selector: 'app-sprint-page',
  imports: [
    MatCardModule,
    MatButtonModule,
    CrudTable,
    DatePipe,
    UserListPreviewButton,
    SprintStatusChip,
    TicketStatusChip,
    EntityCard
],
  templateUrl: './sprint-page.html',
  styleUrl: './sprint-page.css',
})
export class SprintPage {
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

  clientDetails!: ClientDetails;
  
  sprintDetails: SprintDetails = {
    id: -1,
    title: "PLACEHOLDER",
    description: "PLACEHOLDER",
    startDate: new Date(),
    dueDate: new Date(),
    status: "PLANNED",
  };

  previewParticipants: UserSummary[] = [];

  @ViewChild("ticketStatusTemplate", { static: true })
  ticketStatusTemplate!: TemplateRef<TicketStatusCellContext>;
  
  columns!: TableColumn<TicketSummary, TicketStatusCellContext>[];
  rowClass = (ticket: TicketSummary) => ({
    "row-normal":
      ticket.status === "IN_PROGRESS"
      || ticket.status === "IN_TESTING"
      ,
    "row-unimportant": 
      ticket.status === "BACKLOG"
      || ticket.status === "COMPLETED"
      ,
  });
  filters: string[] = [
    "Ticket",
    "Status",
    "User"
  ];

  sprintUserFetcher = (query: PageQuery) => 
    this.sprintService.getUserSummaryList(this.sprintDetails.id, query);

  ngOnInit() {
    this.clientDetails = this.route.snapshot.data["clientDetails"];

    this.columns = [
      { 
        columnDef: "id", 
        header: "ID", cell: 
        ticket => ticket.id.toString() 
      },
      { 
        columnDef: "title", 
        header: "Title", cell: 
        ticket => ticket.title 
      },
      {
        columnDef: "status",
        header: "Status",
        cellTemplate: this.ticketStatusTemplate,
        cellContext: ticket => ({
          $implicit: ticket.status
        })
      },
    ];

    this.route.params.subscribe(params => {
      this.loadSprintDetails(params["id"])
    });
  }

  loadSprintDetails(id: number) {
    this.sprintService.getDetailsById(id).subscribe({
      next: (response) => {
        this.sprintDetails = response;
        this.loadPreviewParticipants();

        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error("Could not load sprint!", error);
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
        console.error("Could not load sprint participants!", error);
        this.dialogService.openErrorDialog(
          "Could not load sprint participants!\nPlease try again later!", 
          null
        );
      }
    });
  }

  updateSprint = (sprint: SprintDetails) => {
    this.router.navigate(["/sprint/update/" + sprint.id]);
  }
  deleteSprint = (sprint: SprintDetails) => {
    return this.sprintService.deleteById(
      sprint.id
    );
  }

  loadTickets = (pageQuery: PageQuery) => {
    return this.sprintService.getTicketSummaryList(
      this.sprintDetails.id,
      pageQuery
    );
  }
  viewTicket = (ticket: TicketSummary) => {
    this.router.navigate(["/ticket/" + ticket.id]);
  }
  createTicket = () => {
    this.router.navigate(["/ticket/create"], 
      { queryParams: { sprintId: this.sprintDetails.id } }
    );
  }
  updateTicket = (ticket: TicketSummary) => {
    this.router.navigate(["/ticket/update/" + ticket.id]);
  }
  deleteTicket = (ticket: TicketSummary) => {
    return this.ticketService.deleteById(ticket.id);
  }

  onOpenParticipantList() {
    this.dialog.open(UserListModal, {
      maxWidth: "600px",
      width: "600px",
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
}
