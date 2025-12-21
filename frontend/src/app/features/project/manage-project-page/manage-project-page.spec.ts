import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageProjectPage } from './manage-project-page';

describe('ManageProjectPage', () => {
  let component: ManageProjectPage;
  let fixture: ComponentFixture<ManageProjectPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageProjectPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageProjectPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
