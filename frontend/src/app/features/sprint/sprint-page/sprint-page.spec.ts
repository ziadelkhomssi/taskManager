import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SprintPage } from './sprint-page';

describe('SprintPage', () => {
  let component: SprintPage;
  let fixture: ComponentFixture<SprintPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SprintPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SprintPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
