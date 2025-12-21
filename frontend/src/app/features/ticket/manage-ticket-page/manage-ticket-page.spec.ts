import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageTicketPage } from './manage-ticket-page';

describe('ManageTicketPage', () => {
  let component: ManageTicketPage;
  let fixture: ComponentFixture<ManageTicketPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageTicketPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageTicketPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
