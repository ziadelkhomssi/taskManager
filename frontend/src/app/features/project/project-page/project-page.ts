import { ChangeDetectorRef, Component, TemplateRef, ViewChild } from '@angular/core';
import { ClientDetails, ProjectDetails, SprintDetails, SprintSummary, UserSummary } from '../../../core/ng-openapi';
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
import { ErrorDialog } from '../../../shared/component/dialog/error-dialog';
import { ConfirmDialog } from '../../../shared/component/dialog/confirm-dialog';
import { DialogService } from '../../../core/services/dialog-service';
import { ProjectStatusChip } from "../../../shared/component/status-chip/project-status-chip/project-status-chip";
import { SprintStatusChip } from '../../../shared/component/status-chip/sprint-status-chip/sprint-status-chip';
import { CrudTable, TableColumn } from '../../../shared/component/crud-table/crud-table';
import { EntityCard } from "../../../shared/component/entity-card/entity-card";

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
    CrudTable,
    FallbackImage,
    ProjectStatusChip,
    SprintStatusChip,
    EntityCard
],
  templateUrl: './project-page.html',
  styleUrl: './project-page.css',
})
export class ProjectPage {
  constructor(
    private projectService: ProjectService, 
    private sprintService: SprintService,
    private dialogService: DialogService,
    private changeDetectorRef: ChangeDetectorRef,
    private dialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  clientDetails!: ClientDetails;

  projectDetails: ProjectDetails = {
    id: -1,
    title: "PLACEHOLDER",
    description: "PLACEHOLDER",
    status: "PLANNED",
  };

  previewParticipants: UserSummary[] = [];
  projectUserFetcher = (query: PageQuery) => 
    this.projectService.getUserSummaryList(
      this.projectDetails.id, query
    );

  @ViewChild("sprintStatusTemplate", { static: true })
  sprintStatusTemplate!: TemplateRef<SprintStatusCellContext>;
  
  rowClass = (sprint: SprintSummary) => ({
    "row-important": 
      new Date(sprint.dueDate).getTime() < Date.now() && sprint.status == "ACTIVE"
      ,
    "row-normal":
      (sprint.status === "ACTIVE" && !(new Date(sprint.dueDate).getTime() < Date.now()))
      || sprint.status === "PAUSED"
      ,
    "row-unimportant": 
      sprint.status === "DONE"
      || sprint.status === "PLANNED"
      ,
  });
  columns!: TableColumn<SprintSummary, SprintStatusCellContext>[];
  filters: string[] = [
    "Sprint",
    "Status",
    "Ticket",
    "User"
  ];

  cacheBuster = Date.now().toString();

  ngOnInit() {
    this.clientDetails = this.route.snapshot.data["clientDetails"];

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
        this.loadPreviewParticipants();

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

  updateProject = (project: ProjectDetails) => {
    console.log("updating!")
    this.router.navigate(["/project/update/" + project.id]);
  }
  deleteProject = (project: ProjectDetails) => {
    return this.projectService.deleteById(
      project.id
    );
  }

  loadSprints = (pageQuery: PageQuery) => {
    return this.projectService.getSprintSummaryList(
      this.projectDetails.id,
      pageQuery
    );
  }
  viewSprint = (sprint: SprintSummary) => {
    this.router.navigate(["/sprint/" + sprint.id]);
  }
  createSprint = () => {
    this.router.navigate(
      ["/sprint/create"], 
      {queryParams: {projectId: this.projectDetails.id} }
    );
  }
  updateSprint = (sprint: SprintSummary) => {
    this.router.navigate(
      ["/sprint/update/" + sprint.id], 
      {queryParams: {projectId: this.projectDetails.id} }
    );
  }
  deleteSprint = (sprint: SprintSummary) => {
    return this.sprintService.deleteById(sprint.id);
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
}
