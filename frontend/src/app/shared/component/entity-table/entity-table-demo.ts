import { MatTableModule } from "@angular/material/table";
import { EntityTable, TableAction, TableColumn } from "./entity-table";
import { Component } from "@angular/core";
import { MatPaginatorModule, PageEvent } from "@angular/material/paginator";
import { MatButtonModule } from "@angular/material/button";
import { UserSummary } from "../../../core/ng-openapi";
import { PageQuery } from "../../types/types";

@Component({
  standalone: true,
  selector: 'app-entity-table-demo',
  imports: [
    EntityTable,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule
  ],
  template: `
    <app-entity-table
      [rows]="rows"
      [columns]="columns"
      [actions]="actions"
      [showAddButton]="true"
      [filters]="filters"
      [totalElements]="rows.length"
      [pageIndex]="pageIndex"
      [pageSize]="pageSize"
      (pageQueryEvent)="onPageQuery($event)"
      (rowClick)="onRowClick($event)"
      (addClick)="onAdd()">
    </app-entity-table>
  `
})
export class EntityTableDemo {
    pageIndex = 0;
    pageSize = 10;
    
    rows: UserSummary[] = [
        { id: "abc1", name: "John", profilePicture: "profilePictureJohn.png" },
        { id: "def2", name: "Jane", profilePicture: "profilePictureJane.png" },
        { id: "ghi3", name: "Steve", profilePicture: "profilePictureJohn.png" },
        { id: "jkl4", name: "Alex", profilePicture: "profilePictureJane.png" },
        { id: "mno5", name: "Sonic", profilePicture: "profilePictureJohn.png" },
        { id: "pqr6", name: "Tails", profilePicture: "profilePictureJane.png" },
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
            cell: (row: UserSummary) => row.profilePicture ?? "",
        },
    ];
    
    actions: TableAction<UserSummary>[] = [
        {
            label: "Edit",
            color: "accent",
            callback: (row: UserSummary) => this.onEdit(row),
        },
        {
            label: "Delete",
            color: "warn",
            callback: (row: UserSummary) => this.onDelete(row),
        },
    ];
    
    filters: string[] = [
        "Filter 1",
        "Filter 2",
        "Filter 3"
    ];
    
    onPageQuery(event: PageQuery) {
        console.log("Page changed", event);
    }
    
    onRowClick(row: UserSummary) {
        console.log("Row clicked", row);
    };

    onAdd() {
        console.log("Add action pressed!");
    }
    onEdit(row: UserSummary) {
        console.log("Edit action pressed! Row:", row);
    }
    onDelete(row: UserSummary) {
        console.log("Delete action pressed! Row:", row);
    }
}
