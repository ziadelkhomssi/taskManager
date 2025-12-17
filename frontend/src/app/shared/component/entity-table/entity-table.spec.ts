import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntityTable } from './entity-table';
import { MatPaginator } from '@angular/material/paginator';
import { MatTable } from '@angular/material/table';

describe('EntityTable', () => {
  let component: EntityTable<any>;
  let fixture: ComponentFixture<EntityTable<any>>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        EntityTable,
        MatTable,
        MatPaginator,
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EntityTable<any>);
    component = fixture.componentInstance;

    component.columns = [
      {
        columnDef: 'name',
        header: 'Name',
        cell: (row: any) => row.name
      }
    ];

    component.rows = [
      { name: 'Test Entity' }
    ];

    component.actions = [];

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
