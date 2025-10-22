import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserDashboardContentComponent } from './user-dashboard-content.component';

describe('UserDashboardContentComponent', () => {
  let component: UserDashboardContentComponent;
  let fixture: ComponentFixture<UserDashboardContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserDashboardContentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserDashboardContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
