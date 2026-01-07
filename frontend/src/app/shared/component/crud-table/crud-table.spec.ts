import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudTable } from './crud-table';

describe('CrudTable', () => {
  let component: CrudTable<any>;
  let fixture: ComponentFixture<CrudTable<any>>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrudTable]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrudTable);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
