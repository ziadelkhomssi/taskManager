import { Component, Input } from '@angular/core';
import { SprintDetails } from '../../../../core/ng-openapi';

@Component({
  selector: 'app-sprint-status-chip',
  imports: [],
  templateUrl: './sprint-status-chip.html',
  styleUrl: './sprint-status-chip.css',
})
export class SprintStatusChip {
  @Input() status!: SprintDetails["status"]
}
