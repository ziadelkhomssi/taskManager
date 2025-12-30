import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketCommentForm } from './ticket-comment-form';

describe('TicketCommentForm', () => {
  let component: TicketCommentForm;
  let fixture: ComponentFixture<TicketCommentForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketCommentForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketCommentForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
