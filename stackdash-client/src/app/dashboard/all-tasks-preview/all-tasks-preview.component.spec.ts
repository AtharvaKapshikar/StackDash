import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllTasksPreviewComponent } from './all-tasks-preview.component';

describe('AllTasksPreviewComponent', () => {
  let component: AllTasksPreviewComponent;
  let fixture: ComponentFixture<AllTasksPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AllTasksPreviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllTasksPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
