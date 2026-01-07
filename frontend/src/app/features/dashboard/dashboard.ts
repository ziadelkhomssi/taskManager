import { ChangeDetectorRef, Component, TemplateRef, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material/table';
import { EntityTable, TableColumn } from '../../shared/component/entity-table/entity-table';
import { ClientDetails, ProjectDetails, ProjectSummary } from '../../core/ng-openapi';
import { ProjectService } from '../../core/services/project-service';
import { response } from 'express';
import { PageEvent } from '@angular/material/paginator';
import { error } from 'console';
import { ActivatedRoute, Router } from '@angular/router';
import { PageQuery } from '../../shared/types/types';
import { ProjectStatusChip } from "../../shared/component/status-chip/project-status-chip/project-status-chip";
import { DialogService } from '../../core/services/dialog-service';
import { CrudTable } from '../../shared/component/crud-table/crud-table';

export interface ProjectStatusCellContext {
  $implicit: ProjectDetails["status"];
}

const DEFAULT_PROJECTS_QUERY: PageQuery = {
  page: 0,
  size: 10,
  search: "",
  filter: ""
}

@Component({
  selector: 'app-dashboard',
  imports: [
    CrudTable,
    ProjectStatusChip
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  clientDetails!: ClientDetails;

  @ViewChild("projectStatusTemplate", { static: true })
  projectStatusTemplate!: TemplateRef<ProjectStatusCellContext>;

  columns!: TableColumn<ProjectSummary, ProjectStatusCellContext>[];
  rowClass = (project: ProjectSummary) => ({
    "row-normal":
      project.status === "ACTIVE"
      ,
    "row-unimportant": 
      project.status === "DONE"
      || project.status === "PLANNED"
      || project.status === "ARCHIVED"
      ,
  });
  filters: string[] = [
    "Project",
    "Status",
    "Sprint",
    "User"
  ];

  constructor(
    private projectService: ProjectService,
    private dialogService: DialogService,
    private changeDetectorRef: ChangeDetectorRef,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.clientDetails = this.route.snapshot.data["clientDetails"];

    this.columns = [
      {
        columnDef: "id",
        header: "ID", cell:
          project => project.id.toString()
      },
      {
        columnDef: "title",
        header: "Title",
        cell: project => project.title
      },
      {
        columnDef: "status",
        header: "Status",
        cellTemplate: this.projectStatusTemplate,
        cellContext: project => ({
          $implicit: project.status
        })
      }
    ];
  }

  loadProjects = (pageQuery: PageQuery) => {
    return this.projectService.getSummaryList(pageQuery)
  }
  viewProject = (project: ProjectSummary) => {
    this.router.navigate(["/project/" + project.id])
  }
  createProject = () => {
    this.router.navigate(["/project/create"])
  }
  updateProject = (project: ProjectSummary) => {
    this.router.navigate(["/project/update/" + project.id])
  }
  deleteProject = (project: ProjectSummary) => {
    return this.projectService.deleteById(project.id);
  }
}
