import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserTasksPreviewComponent } from './user-tasks-preview.component';

describe('UserTasksPreviewComponent', () => {
  let component: UserTasksPreviewComponent;
  let fixture: ComponentFixture<UserTasksPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserTasksPreviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserTasksPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
