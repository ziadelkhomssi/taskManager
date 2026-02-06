import { MatTableModule } from "@angular/material/table";
import { Component } from "@angular/core";
import { MatPaginatorModule, PageEvent } from "@angular/material/paginator";
import { MatButtonModule } from "@angular/material/button";
import { UserSummary } from "../../../core/ng-openapi";
import { PageQuery } from "../../types/types";
import { CrudTable, TableColumn } from "./crud-table";
import { of } from "rxjs";
import { TitleCasePipe } from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-crud-table-demo',
  imports: [
    CrudTable,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule
  ],
  template: `
    <app-crud-table
      [pageQuery]="pageQuery"
      [columns]="columns"
      [filters]="filters"
      [showManipulateOptions]="true"
      [create]="onCreate"
      [update]="onUpdate"
      [delete]="onDelete"
      [view]="onView"
      />
  `
})
export class CrudTableDemo {
    pageIndex = 0;
    pageSize = 10;
    
    readonly rows: UserSummary[] = [
        { id: "abc1", name: "John", profilePictureUrl: "profilePictureJohn.png" },
        { id: "def2", name: "Jane", profilePictureUrl: "profilePictureJane.png" },
        { id: "ghi3", name: "Steve", profilePictureUrl: "profilePictureJohn.png" },
        { id: "jkl4", name: "Alex", profilePictureUrl: "profilePictureJane.png" },
        { id: "mno5", name: "Sonic", profilePictureUrl: "profilePictureJohn.png" },
        { id: "pqr6", name: "Tails", profilePictureUrl: "profilePictureJane.png" },
    ];

    columns: TableColumn<UserSummary>[] = [
        {
            columnDef: "id",
            header: "ID",
            cell: (row: UserSummary) => row.id,
        },
        {
            columnDef: "name",
            header: "Name",
            cell: (row: UserSummary) => row.name,
        },
        {
            columnDef: "profilePicture",
            header: "Profile Picture",
            cell: (row: UserSummary) => row.profilePictureUrl ?? "",
        },
    ];
    
    filters: string[] = [
        "Filter 1",
        "Filter 2",
        "Filter 3"
    ];
    

    pageQuery = (pageQuery: PageQuery) => {
        return of({
            totalElements: this.rows.length,
            totalPages: 1,
            size: 10,
            content: this.rows,
            number: 0,
            pageable: {
                offset: 0,
                paged: true,
                pageNumber: 0,
                pageSize: 10,
                sort: {
                    empty: false,
                    sorted: false,
                    unsorted: true,
                },
                unpaged: false,
            },
            sort: {
                empty: false,
                sorted: false,
                unsorted: true,
            },
            numberOfElements: this.rows.length,
            first: true,
            last: false,
            empty: false
        });
    }
    
    onView(row: UserSummary) {
        console.log("Row clicked", row);
    };
    onCreate() {
        console.log("Add action pressed!");
    }
    onUpdate(row: UserSummary) {
        console.log("Edit action pressed! Row:", row);
    }
    onDelete(row: UserSummary) {
        console.log("Delete action pressed! Row:", row);
        return of();
    }
}
