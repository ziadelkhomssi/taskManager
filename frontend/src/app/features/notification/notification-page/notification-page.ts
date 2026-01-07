import { ChangeDetectorRef, Component, TemplateRef, ViewChild } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { NotificationDetails, TicketDetails } from '../../../core/ng-openapi';
import { PageQuery } from '../../../shared/types/types';
import { NotificationService } from '../../../core/services/notification-service';
import { DialogService } from '../../../core/services/dialog-service';
import { Router } from '@angular/router';
import { TicketStatusChip } from "../../../shared/component/status-chip/ticket-status-chip/ticket-status-chip";
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { TableColumn } from '../../../shared/component/crud-table/crud-table';

export interface TicketStatusCellContext {
  $implicit: TicketDetails["status"];
}

const MINIMUM_HOVER_DURATION_BEFORE_BECOMES_READ_MILLISECONDS: number = 500;

const DEFAULT_NOTIFICATIONS_QUERY: PageQuery = {
  page: 0,
  size: 10,
  search: "",
  filter: ""
}

@Component({
  selector: 'app-notification-page',
  imports: [
    MatTableModule,
    MatButtonModule,
    MatPaginatorModule,
		CommonModule
  ],
  templateUrl: './notification-page.html',
  styleUrl: './notification-page.css',
})
export class NotificationPage {
  notifications: NotificationDetails[] = [];

  columns: TableColumn<NotificationDetails>[] = [
      {
        columnDef: "readIndicator",
        header: "",
        cell: () => ""
      },
      {
        columnDef: "source",
        header: "Source",
        cell: notification => notification.ticketSummary.title
      },
      {
        columnDef: "notification",
        header: "Notification",
        cell: notification => this.generateNotificationText(notification)
      }
    ];

	pageIndex = 0;
	pageSize = 10;
	totalElements = 0;
	pageSizeOptions: number[] = [5, 10, 20];

  lastQuery: PageQuery = DEFAULT_NOTIFICATIONS_QUERY;
  filters: string[] = [
    "Notification",
    "Status",
    "Priority",
  ];

  private hoverTimer: any;

  constructor(
    private notificationService: NotificationService,
    private dialogService: DialogService,
    private changeDetectorRef: ChangeDetectorRef,
    private location: Location,
    private router: Router
  ) { }

  ngOnInit() {
    this.loadNotifications(DEFAULT_NOTIFICATIONS_QUERY);
  }

  loadNotifications(pageQuery: PageQuery) {
    this.notificationService.getAllForClient(pageQuery).subscribe({
      next: (response) => {
        this.notifications = response.content || [];
        this.pageIndex = response.pageable?.pageNumber || 0;
        this.pageSize = response.pageable?.pageSize || 0;
        this.totalElements = response.totalElements || 0;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error("Could not load notifications!", error);
        this.dialogService.openErrorDialog(
          "Could not load notifications! Try again later!",
          null
        );
        this.notifications = [];
        this.pageIndex = 0;
        this.pageSize = 0;
        this.totalElements = 0;
        this.changeDetectorRef.detectChanges();
      }
    })
  }

  generateNotificationText(notification: NotificationDetails): string {
    switch(notification.type) {
      case "TICKET_ASSIGNED": return "You have been assigned this ticket."
      case "TICKET_STATUS_CHANGED": return "This ticket had it's status changed."
      default: {
        console.error("Invalid notification type!");
      }
    }

    return "SOMETHING WENT WRONG";
  }

  onRowMouseEnter(notification: NotificationDetails) {
    if (this.hoverTimer) {
      clearTimeout(this.hoverTimer);
    }

    this.hoverTimer = setTimeout(() => {
      this.onHoverToRead(notification);
    }, MINIMUM_HOVER_DURATION_BEFORE_BECOMES_READ_MILLISECONDS);
  }

  onRowMouseLeave() {
    if (this.hoverTimer) {
      clearTimeout(this.hoverTimer);
      this.hoverTimer = null;
    }
  }

  onHoverToRead(notification: NotificationDetails) {
    if (notification.isRead) {return;}
    this.notificationService.markAsRead(notification.id).subscribe({
      next: () => {
        var localSource: NotificationDetails | undefined = this.notifications.find(
          source => source.id === notification.id
        );
        if (localSource == undefined) {
          // last resort
          this.loadNotifications(DEFAULT_NOTIFICATIONS_QUERY);
          return;
        }
        localSource.isRead = true;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.log("Failed to mark notification as read!", error);
        this.dialogService.openErrorDialog(
          "Failed to mark notification as read! Try again later!",
          null
        );
      }
    });
  }

  onPageChange(event: PageEvent) {
		this.pageIndex = event.pageIndex;
		this.pageSize = event.pageSize;
    this.lastQuery = {
      page: this.pageIndex,
      size: this.pageSize,
      search: "",
      filter: ""
    }
	}

  onRowClick(notification: NotificationDetails) {
    this.viewNotification(notification)
  }

  viewNotification(notification: NotificationDetails) {
    this.notificationService.markAsRead(notification.id).subscribe({
      next: () => {},
      error: (error) => {
        console.log("Failed to mark notification as read!", error);
      }
    });
    this.router.navigate(["/ticket/" + notification.ticketSummary.id]);
  }

  goBack() {
    this.location.back();
  }

  get displayedColumns(): string[] {
    const columnsGuard = this.columns ?? [];

    return [
      ...columnsGuard.map(c => c.columnDef)
    ];
	}
}
