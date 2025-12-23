import { ChangeDetectorRef, Component, Inject, Input } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { PageUserSummary, UserSummary } from '../../../core/ng-openapi';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatPaginatorModule } from '@angular/material/paginator';
import { PageQuery, UserFetcher } from '../../types/types';
import { SearchBar, SearchQuery } from "../search-bar/search-bar";
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { FallbackImage } from '../../directive/fallback-image/fallback-image';

@Component({
  selector: 'app-user-list-modal',
  imports: [
    SearchBar,
    MatDialogModule,
    MatProgressBarModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    FallbackImage
],
  templateUrl: './user-list-modal.html',
  styleUrl: './user-list-modal.css',
})
export class UserListModal {
  users: UserSummary[] = [];
  filters: string[] = [
    "Name",
    "Job"
  ];

  loading = false;

  pageIndex = 0;
	pageSize = 10;
	totalElements = 0;
	pageSizeOptions: number[] = [5, 10, 20];

  lastQuery: SearchQuery = {
    search: "",
    filter: this.filters[0]
  };

  description: string = "";

  constructor(
    private changeDetectorRef: ChangeDetectorRef,
    private dialogRef: MatDialogRef<UserListModal>,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      fetchUsers: UserFetcher;
      onUserClick?: (dialogRef: MatDialogRef<UserListModal>, user: UserSummary) => void;
      description?: string
    }
  ) {
    this.description = data.description || "";
  }

  ngOnInit(): void {
    this.loadPage({
      page: 0,
      size: 10,
      search: "",
      filter: "name"
    });
  }

  loadPage(pageQuery: PageQuery): void {
    this.loading = true;

    this.data.fetchUsers(pageQuery).subscribe({
      next: (response: PageUserSummary) => {
        this.users = response.content || [];
        this.pageIndex = response.pageable?.pageNumber || 0;
        this.pageSize = response.pageable?.pageSize || 0;
        this.totalElements = response.totalElements || 0;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        console.log("Could not load users!")
        this.loading = false;
      }
    });
  }

  onQuery(query: SearchQuery) {
		this.lastQuery = query;

    this.loadPage({
			page: this.pageIndex,
			size: this.pageSize,
			search: query.search,
			filter: query.filter
    });
  }

  onPageChange(event: any): void {
    this.loadPage({
			page: event.pageIndex,
			size: event.pageSize,
			search: this.lastQuery.search,
			filter: this.lastQuery.filter
		});
  }

  onRowClick(user: UserSummary) {
    if (!this.data.onUserClick) {
      return;
    }

    this.data.onUserClick(this.dialogRef, user);
  }

  close(): void {
    this.dialogRef.close();
  }
}
