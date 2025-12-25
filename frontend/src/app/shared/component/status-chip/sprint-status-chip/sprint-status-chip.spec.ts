import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SprintStatusChip } from './sprint-status-chip';

describe('SprintStatusChip', () => {
  let component: SprintStatusChip;
  let fixture: ComponentFixture<SprintStatusChip>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SprintStatusChip]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SprintStatusChip);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
