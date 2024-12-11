import { TestBed } from '@angular/core/testing';

import { AllegianceService } from './allegiance.service';

describe('AllegianceService', () => {
  let service: AllegianceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AllegianceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
