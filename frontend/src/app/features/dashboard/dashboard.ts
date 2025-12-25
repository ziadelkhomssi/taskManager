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

export interface ProjectStatusCellContext {
  $implicit: ProjectDetails["status"];
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

  actions = [
    {
      label: 'Update',
      callback: (project: ProjectSummary) => this.updateProject(project)
    },
    {
      label: 'Delete',
      callback: (project: ProjectSummary) => this.deleteProject(project)
    },
  ];

  filters: string[] = [
    "Project",
    "Status",
    "Sprint",
    "User"
  ];

  pageIndex = 0;
  pageSize = 10;
  totalElements = 0;

  constructor(
    private projectService: ProjectService, 
    private changeDetectorRef: ChangeDetectorRef,
    private router: Router
  ) { }

  ngOnInit() {
    this.columns = [
      { 
        columnDef: 'id', 
        header: 'ID', cell: 
        project => project.id.toString() 
      },
      { 
        columnDef: 'title', 
        header: 'Title', 
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

    this.loadProjects(
      {
        page: 0,
        size: 10,
        search: "",
        filter: this.filters[0]
      }
    );
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
        console.error('Could not load projects!', error);
        this.projects = [];
        this.pageIndex = 0;
        this.pageSize = 0;
        this.totalElements = 0;
        this.changeDetectorRef.detectChanges();
      }
    })
  }

  onPageQuery(pageQuery: PageQuery) {
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
    this.projectService.deleteById(project.id).subscribe({
      next: (response) => {
        console.log("project deleted!")
        //this.loadProjects();
      },
      error: (error) => {
        console.log("Could not delete project!", error)
      }
    });
  }
}
