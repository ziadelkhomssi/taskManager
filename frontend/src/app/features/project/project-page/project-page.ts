import { ChangeDetectorRef, Component, TemplateRef, ViewChild } from '@angular/core';
import { ProjectDetails, SprintDetails, SprintSummary, UserSummary } from '../../../core/ng-openapi';
import { EntityTable, TableColumn } from '../../../shared/component/entity-table/entity-table';
import { SprintService } from '../../../core/services/sprint-service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectService } from '../../../core/services/project-service';
import { FallbackImage } from '../../../shared/directive/fallback-image/fallback-image';
import { CommonModule, formatDate } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { PageQuery } from '../../../shared/types/types';
import { UserListPreviewButton } from '../../../shared/component/user-list-preview-button/user-list-preview-button';
import { UserListModal } from '../../../shared/component/user-list-modal/user-list-modal';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ErrorDialog } from '../../../shared/component/error-dialog/error-dialog';
import { ConfirmDialog } from '../../../shared/component/confirm-dialog/confirm-dialog';
import { DialogService } from '../../../core/services/dialog-service';
import { ProjectStatusChip } from "../../../shared/component/status-chip/project-status-chip/project-status-chip";
import { SprintStatusChip } from '../../../shared/component/status-chip/sprint-status-chip/sprint-status-chip';

const DEFAULT_PREVIEW_PARTICIPANTS_QUERY: PageQuery = {
  page: 0,
  size: 3,
  search: "",
  filter: ""
}

const DEFAULT_SPRINTS_QUERY: PageQuery = {
  page: 0,
  size: 10,
  search: "",
  filter: ""
}

export interface SprintStatusCellContext {
  $implicit: SprintDetails["status"];
}

@Component({
  selector: 'app-project-page',
  imports: [
    UserListPreviewButton,
    MatCardModule,
    MatButtonModule,
    EntityTable,
    FallbackImage,
    ProjectStatusChip,
    SprintStatusChip
],
  templateUrl: './project-page.html',
  styleUrl: './project-page.css',
})
export class ProjectPage {
  projectDetails: ProjectDetails = {
    id: -1,
    title: "PLACEHOLDER",
    description: "PLACEHOLDER",
    status: "PLANNED",
  };

  previewParticipants: UserSummary[] = [];

  sprints: SprintSummary[] = [];

  @ViewChild("sprintStatusTemplate", { static: true })
  sprintStatusTemplate!: TemplateRef<SprintStatusCellContext>;
  
  columns!: TableColumn<SprintSummary, SprintStatusCellContext>[];

  actions = [
    {
      label: "Update",
      callback: (sprint: SprintSummary) => this.updateSprint(sprint)
    },
    {
      label: "Delete",
      callback: (sprint: SprintSummary) => this.deleteSprint(sprint)
    },
  ];

  filters: string[] = [
    "Sprint",
    "Status",
    "Ticket",
    "User"
  ];

  lastSprintsQuery: PageQuery = DEFAULT_SPRINTS_QUERY;
  pageIndex = 0;
  pageSize = 10;
  totalElements = 0;

  cacheBuster = Date.now().toString();

  projectUserFetcher = (query: PageQuery) => 
    this.projectService.getUserSummaryList(
      this.projectDetails.id, query
    );

  constructor(
    private projectService: ProjectService, 
    private sprintService: SprintService, 
    private dialogService: DialogService,
    private changeDetectorRef: ChangeDetectorRef,
    private dialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.cacheBuster = Date.now().toString();
    this.columns = [
      {
        columnDef: "id",
        header: "ID",
        cell: sprint => sprint.id.toString()
      },
      {
        columnDef: "title",
        header: "Title",
        cell: sprint => sprint.title
      },
      {
        columnDef: "status",
        header: "Status",
        cellTemplate: this.sprintStatusTemplate,
        cellContext: sprint => ({
          $implicit: sprint.status
        })
      },
      {
        columnDef: "dueDate",
        header: "Due Date",
        cell: sprint => formatDate(new Date(sprint.dueDate), "yyyy-MM-dd", "en-US")
      }
    ];

    this.route.params.subscribe(params => {
      this.loadProjectDetails(params["id"])
    });
  }

  loadProjectDetails(id: number) {
    this.projectService.getDetailsById(id).subscribe({
      next: (response) => {
        this.projectDetails = response;

        this.projectUserFetcher = (query: PageQuery) => 
          this.projectService.getUserSummaryList(
            this.projectDetails.id, query
          );

        this.loadPreviewParticipants();
        this.loadSprints(DEFAULT_SPRINTS_QUERY);

        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error("Could not load project!", error);
        this.dialogService.openErrorDialog(
          "Could not load project!\nPlease try again later!",
          () => {this.router.navigate(["/"])}
        );
      }
    });
  }

  loadPreviewParticipants() {
    this.projectUserFetcher(DEFAULT_PREVIEW_PARTICIPANTS_QUERY).subscribe({
      next: (response) => {
        this.previewParticipants = response.content || [];
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error("Could not load project participants!", error);
        this.dialogService.openErrorDialog("Could not load project participants!\nPlease try again later!", null);
      }
    });
  }

  loadSprints(pageQuery: PageQuery) {
    this.projectService.getSprintSummaryList(
      this.projectDetails.id,
      pageQuery
    ).subscribe({
      next: (response) => {
        this.sprints = response.content || [];
        this.pageIndex = response.pageable?.pageNumber || 0;
        this.pageSize = response.pageable?.pageSize || 0;
        this.totalElements = response.totalElements || 0;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error("Could not load sprints!", error);
        this.sprints = [];
        this.pageIndex = 0;
        this.pageSize = 0;
        this.totalElements = 0;
        this.changeDetectorRef.detectChanges();

        this.dialogService.openErrorDialog("Could not load sprints!\nPlease try again later!", null);
      }
    })
  }

  updateProject() {
    this.router.navigate(["/project/update/" + this.projectDetails.id]);
  }
  deleteProject() {
    this.dialogService.openConfirmDialog(
      "Delete Current Project?", null, null, null,
      () => {
        this.projectService.deleteById(
          this.projectDetails.id
        ).subscribe({
          next: () => {
            this.router.navigate(["/"])
          },
          error: (error) => {
            console.error("Could not delete project!", error);
            this.dialogService.openErrorDialog("Could not delete project!\nPlease try again later!", null);
          }
    });});
  }

  onPageQuery(pageQuery: PageQuery) {
    this.lastSprintsQuery = pageQuery;
    this.loadSprints(pageQuery);
  }

  onRowClick(sprint: SprintSummary) {
    this.viewSprint(sprint);
  }

  onOpenParticipantList() {
    this.dialog.open(UserListModal, {
      maxWidth: "600px",
      width: "600px",
      data: {
        fetchUsers: this.projectUserFetcher,
        onUserClick: (dialogRef: MatDialogRef<UserListModal>, user: UserSummary) => {
          dialogRef.close();
          this.router.navigate(["/user/" + user.id]);
        },
        description: "Project Participants"
      }
    });
  }

  viewSprint(sprint: SprintSummary) {
    this.router.navigate(["/sprint/" + sprint.id]);
  }
  createSprint() {
    this.router.navigate(["/sprint/create"], {queryParams: {projectId: this.projectDetails.id} });
  }
  updateSprint(sprint: SprintSummary) {
    this.router.navigate(["/sprint/update/" + sprint.id], {queryParams: {projectId: this.projectDetails.id} });
  }
  deleteSprint(sprint: SprintSummary) {
    this.dialogService.openConfirmDialog(
      `Delete Sprint ${sprint.title}?`, null, null, null,
      () => {
        this.sprintService.deleteById(
          sprint.id
        ).subscribe({
          next: () => {
            this.loadSprints(this.lastSprintsQuery);
          },
          error: (error) => {
            console.error("Could not delete sprint!", error);
            this.dialogService.openErrorDialog(
              "Could not delete sprint!\nPlease try again later!", null
            );
    }});});
  }
}
