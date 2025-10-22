import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateUserTaskComponent } from './update-user-task.component';

describe('UpdateUserTaskComponent', () => {
  let component: UpdateUserTaskComponent;
  let fixture: ComponentFixture<UpdateUserTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateUserTaskComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateUserTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
