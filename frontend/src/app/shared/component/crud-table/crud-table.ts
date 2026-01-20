import { ChangeDetectorRef, Component, EventEmitter, Inject, Input, Output, PLATFORM_ID, TemplateRef } from '@angular/core';
import { PageQuery } from '../../types/types';
import { SearchBar, SearchQuery } from '../search-bar/search-bar';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { PageableObject, SortObject } from '../../../core/ng-openapi';
import { Observable } from 'rxjs';
import { DialogService } from '../../../core/services/dialog-service';
import { Location } from '@angular/common';

export interface Page<T> {
    totalElements?: number;
    totalPages?: number;
    size?: number;
    content?: Array<T>;
    number?: number;
    pageable?: PageableObject;
    sort?: SortObject;
    numberOfElements?: number;
    first?: boolean;
    last?: boolean;
    empty?: boolean;
}

export interface TableAction<T> {
	label: string;
	color?: "primary" | "accent" | "warn";
	callback: (row: T) => void;
}

export interface TableColumn<T, context = any> {
  columnDef: string;
  header: string;
  cell?: (row: T) => string;
  cellTemplate?: TemplateRef<context>;
  cellContext?: (row: T) => context;
}

const DEFAULT_PAGE_QUERY: PageQuery = {
  page: 0,
  size: 10,
  search: "",
  filter: ""
}

@Component({
  selector: 'app-crud-table',
  imports: [
		SearchBar,
		MatTableModule,
		MatPaginatorModule,
		MatButtonModule,
		CommonModule
  ],
  templateUrl: './crud-table.html',
  styleUrl: './crud-table.css',
})
export class CrudTable<T> {
  constructor(
    private dialogService: DialogService,
    private changeDetectorRef: ChangeDetectorRef,
    private location: Location,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  @Input() entityName: string = "Entity";
	@Input() rowClass?: (row: T) => string | string[] | Set<string> | { [klass: string]: boolean };
	@Input() columns: TableColumn<T>[] = [];

	actions: TableAction<T>[] = [
    {
      label: "Update",
      callback: (entity: T) => this.onUpdate(entity)
    },
    {
      label: "Delete",
      callback: (entity: T) => this.onDelete(entity)
    },
  ];

	@Input() filters: string[] = [];

	@Input() showManipulateOptions = false;

  @Input() pageQuery: (pageQuery: PageQuery) => Observable<Page<T>> = () => {
    return new Observable
  };
	@Input() view: (entity: T) => void = () => {};
  @Input() create: () => void = () => {};
  @Input() update: (entity: T) => void = () => {};
  @Input() delete: (entity: T) => Observable<void> = () => {
    return new Observable
  };
  @Input() isLoadData: boolean = false;

	rows: T[] = [];

	pageIndex = 0;
  pageSize = 10;
	totalElements = 0;
	pageSizeOptions: number[] = [5, 10, 20];

  lastPageQuery: PageQuery = DEFAULT_PAGE_QUERY;
	lastSearchQuery: SearchQuery = { 
    search: "", 
    filter: "" 
  };

  ngOnChanges() {
    if (!this.isLoadData) {
      return
    }
    this.loadEntities(this.lastPageQuery);
  }

	onPageChange(event: PageEvent) {
		this.pageIndex = event.pageIndex;
		this.pageSize = event.pageSize;

		this.loadEntities({
			page: event.pageIndex,
			size: event.pageSize,
			search: this.lastSearchQuery.search,
			filter: this.lastSearchQuery.filter
		});
	}

	onQuery(query: SearchQuery) {
		this.lastSearchQuery = query;

		this.loadEntities({
			page: this.pageIndex,
			size: this.pageSize,
			search: query.search,
			filter: query.filter
		});
	}

	loadEntities(pageQuery: PageQuery) {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    this.pageQuery(
      pageQuery
    ).subscribe({
      next: (response) => {
        this.rows = response.content || [];
        this.pageIndex = response.pageable?.pageNumber || 0;
        this.pageSize = response.pageable?.pageSize || 0;
        this.totalElements = response.totalElements || 0;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error(`Could not load ${this.entityName.toLowerCase()}!`, error);
        this.rows = [];
        this.pageIndex = 0;
        this.pageSize = 0;
        this.totalElements = 0;
        this.changeDetectorRef.detectChanges();

        this.dialogService.openErrorDialog(
          `Could not load ${this.entityName.toLowerCase()}!\nPlease try again later!`, 
          null
        );
      }
    })
  }

	onRowClick(row: T) {
		this.view(row);
	}

  onCreate() {
    this.create();
  }

  onUpdate(row: T) {
    this.update(row);
  }

  onDelete(row: T) {
    this.dialogService.openConfirmDialog(
      `Delete current ${this.entityName.toLowerCase()}?`, 
      null, null, null,
      () => {
        this.delete(row).subscribe({
          next: () => {
            this.location.back();
          },
          error: (error) => {
            console.error(`Could not delete ${this.entityName.toLowerCase()}!`, error);
            this.dialogService.openErrorDialog(`Could not delete ${this.entityName.toLowerCase()}!\nPlease try again later!`, null);
          }
    });});
  }

	get displayedColumns(): string[] {
    const columnsGuard = this.columns ?? [];

    return [
      ...columnsGuard.map(c => c.columnDef),
      ...(this.showManipulateOptions ? ["actions"] : [])
    ];
	}
}
