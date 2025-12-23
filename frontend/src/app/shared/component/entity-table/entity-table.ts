import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatCell, MatCellDef, MatHeaderCell, MatHeaderCellDef, MatHeaderRowDef, MatRowDef, MatTable, MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButton, MatButtonModule } from '@angular/material/button';
import { SearchBar, SearchQuery } from '../search-bar/search-bar';
import { PageQuery } from '../../types/types';

export interface TableAction<T> {
	label: string;
	color?: "primary" | "accent" | "warn";
	callback: (row: T) => void;
}

export interface TableColumn<T> {
	columnDef: string;
	header: string;
	cell: (row: T) => string;
}

@Component({
	selector: 'app-entity-table',
	imports: [
		SearchBar,
		MatTableModule,
		MatPaginatorModule,
		MatButtonModule,
	],
	templateUrl: './entity-table.html',
	styleUrl: './entity-table.css',
})
export class EntityTable<T> {
	@Input() rows: T[] = [];

	@Input() columns: TableColumn<T>[] = [];
	@Input() actions: TableAction<T>[] = [];
	@Input() filters: string[] = [];
	@Input() showAddButton = false;
	@Input() addLabel = 'Add';

	@Input() pageIndex = 0;
	@Input() pageSize = 10;
	@Input() totalElements = 0;
	@Input() pageSizeOptions: number[] = [5, 10, 20];

	@Output() addClick = new EventEmitter<void>();
	@Output() rowClick = new EventEmitter<T>();
	@Output() pageQueryEvent = new EventEmitter<PageQuery>();

	private lastQuery: SearchQuery = { search: '', filter: '' };

	onRowClick(row: T) {
		this.rowClick.emit(row);
	}

	onPageChange(event: PageEvent) {
		this.pageIndex = event.pageIndex;
		this.pageSize = event.pageSize;

		this.emitPageQuery({
			page: event.pageIndex,
			size: event.pageSize,
			search: this.lastQuery.search,
			filter: this.lastQuery.filter
		});
	}

	onQuery(query: SearchQuery) {
		this.lastQuery = query;

		this.emitPageQuery({
			page: this.pageIndex,
			size: this.pageSize,
			search: query.search,
			filter: query.filter
		});
	}

	emitPageQuery(pageQuery: PageQuery) {
		this.pageQueryEvent.emit(pageQuery);
	}

	get displayedColumns(): string[] {
		return [
			...this.columns.map(c => c.columnDef),
			...(this.actions.length ? ["actions"] : [])
		];
	}
}


