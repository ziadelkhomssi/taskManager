import { Component, Input } from '@angular/core';
import { TicketDetails } from '../../../../core/ng-openapi';

@Component({
  selector: 'app-ticket-status-chip',
  imports: [],
  templateUrl: './ticket-status-chip.html',
  styleUrl: './ticket-status-chip.css',
})
export class TicketStatusChip {
  private _status!: string;

  @Input() set status(value: TicketDetails["status"]) {
    switch(value) {
      case "IN_PROGRESS":
        this._status = "IN-PROGRESS";
        return;
      case "IN_TESTING":
        this._status = "IN-TESTING";
        return;
      default:
        this._status = value;
    }
  }
  get status(): string {
      return this._status;
  }
}
