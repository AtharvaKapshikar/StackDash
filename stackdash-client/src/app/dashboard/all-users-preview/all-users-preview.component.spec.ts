import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllUsersPreviewComponent } from './all-users-preview.component';

describe('AllUsersPreviewComponent', () => {
  let component: AllUsersPreviewComponent;
  let fixture: ComponentFixture<AllUsersPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AllUsersPreviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllUsersPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
