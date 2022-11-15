import { TestBed } from '@angular/core/testing';

import { JwttokenService } from './jwttoken.service';

describe('JwttokenService', () => {
  let service: JwttokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JwttokenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
