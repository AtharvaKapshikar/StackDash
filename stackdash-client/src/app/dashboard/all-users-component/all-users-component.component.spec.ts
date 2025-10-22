import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllUsersComponentComponent } from './all-users-component.component';

describe('AllUsersComponentComponent', () => {
  let component: AllUsersComponentComponent;
  let fixture: ComponentFixture<AllUsersComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AllUsersComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllUsersComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
