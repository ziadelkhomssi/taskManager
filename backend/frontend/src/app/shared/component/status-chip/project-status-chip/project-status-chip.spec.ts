import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectStatusChip } from './project-status-chip';

describe('ProjectStatusChip', () => {
  let component: ProjectStatusChip;
  let fixture: ComponentFixture<ProjectStatusChip>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectStatusChip]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjectStatusChip);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
