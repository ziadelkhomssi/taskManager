import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserListPreviewButton } from './user-list-preview-button';

describe('UserListPreviewButton', () => {
  let component: UserListPreviewButton;
  let fixture: ComponentFixture<UserListPreviewButton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserListPreviewButton]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserListPreviewButton);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
