import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketStatusChip } from './ticket-status-chip';

describe('TicketStatusChip', () => {
  let component: TicketStatusChip;
  let fixture: ComponentFixture<TicketStatusChip>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketStatusChip]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketStatusChip);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
