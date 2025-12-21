import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageSprintPage } from './manage-sprint-page';

describe('ManageSprintPage', () => {
  let component: ManageSprintPage;
  let fixture: ComponentFixture<ManageSprintPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageSprintPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageSprintPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
