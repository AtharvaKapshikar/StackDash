import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserAllTaskComponent } from './user-all-task.component';

describe('UserAllTaskComponent', () => {
  let component: UserAllTaskComponent;
  let fixture: ComponentFixture<UserAllTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserAllTaskComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserAllTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
