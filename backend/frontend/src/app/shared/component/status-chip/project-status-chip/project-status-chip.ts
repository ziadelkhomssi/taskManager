import { Component, Input } from '@angular/core';
import { ProjectDetails } from '../../../../core/ng-openapi';

@Component({
  selector: 'app-project-status-chip',
  imports: [],
  templateUrl: './project-status-chip.html',
  styleUrl: './project-status-chip.css',
})
export class ProjectStatusChip {
  @Input() status!: ProjectDetails["status"]
}
