import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketCommentThread } from './ticket-comment-thread';

describe('TicketCommentThread', () => {
  let component: TicketCommentThread;
  let fixture: ComponentFixture<TicketCommentThread>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketCommentThread]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketCommentThread);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
