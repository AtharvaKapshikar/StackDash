import { TestBed } from '@angular/core/testing';

import { SessionLogServiceService } from './session-log-service.service';

describe('SessionLogServiceService', () => {
  let service: SessionLogServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionLogServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
