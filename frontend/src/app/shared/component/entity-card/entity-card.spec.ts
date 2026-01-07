import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntityCard } from './entity-card';

describe('EntityCard', () => {
  let component: EntityCard<any>;
  let fixture: ComponentFixture<EntityCard<any>>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EntityCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EntityCard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
