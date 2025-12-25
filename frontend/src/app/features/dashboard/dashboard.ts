import { ChangeDetectorRef, Component, TemplateRef, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material/table';
import { EntityTable, TableColumn } from '../../shared/component/entity-table/entity-table';
import { ProjectDetails, ProjectSummary } from '../../core/ng-openapi';
import { ProjectService } from '../../core/services/project-service';
import { response } from 'express';
import { PageEvent } from '@angular/material/paginator';
import { error } from 'console';
import { Router } from '@angular/router';
import { PageQuery } from '../../shared/types/types';
import { ProjectStatusChip } from "../../shared/component/status-chip/project-status-chip/project-status-chip";
import { DialogService } from '../../core/services/dialog-service';

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
    EntityTable,
    ProjectStatusChip
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  projects: ProjectSummary[] = [];

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

  actions = [
    {
      label: "Update",
      callback: (project: ProjectSummary) => this.updateProject(project)
    },
    {
      label: "Delete",
      callback: (project: ProjectSummary) => this.deleteProject(project)
    },
  ];

  pageIndex = 0;
  pageSize = 10;
  totalElements = 0;
  lastQuery: PageQuery = DEFAULT_PROJECTS_QUERY;
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
    private router: Router
  ) { }

  ngOnInit() {
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

    this.loadProjects(DEFAULT_PROJECTS_QUERY);
  }

  loadProjects(pageQuery: PageQuery) {
    this.projectService.getSummaryList(pageQuery).subscribe({
      next: (response) => {
        this.projects = response.content || [];
        this.pageIndex = response.pageable?.pageNumber || 0;
        this.pageSize = response.pageable?.pageSize || 0;
        this.totalElements = response.totalElements || 0;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error("Could not load projects!", error);
        this.dialogService.openErrorDialog(
          "Could not load projects! Try again later!",
          null
        );
        this.projects = [];
        this.pageIndex = 0;
        this.pageSize = 0;
        this.totalElements = 0;
        this.changeDetectorRef.detectChanges();
      }
    })
  }

  onPageQuery(pageQuery: PageQuery) {
    this.lastQuery = pageQuery;
    this.loadProjects(pageQuery);
  }

  onRowClick(project: ProjectSummary) {
    this.viewProject(project)
  }

  viewProject(project: ProjectSummary) {
    this.router.navigate(["/project/" + project.id])
  }
  createProject() {
    this.router.navigate(["/project/create"])
  }
  updateProject(project: ProjectSummary) {
    this.router.navigate(["/project/update/" + project.id])
  }
  deleteProject(project: ProjectSummary) {
    this.dialogService.openConfirmDialog(
      `Delete Project ${project.title}?`, null, null, null,
      () => {
        this.projectService.deleteById(project.id).subscribe({
          next: (response) => {
            this.loadProjects(this.lastQuery);
          },
          error: (error) => {
            console.error("Could not delete project!", error)
            this.dialogService.openErrorDialog(
              "Could not delete project! Try again later!",
              null
            );
          }
        });
      });
  }
}
