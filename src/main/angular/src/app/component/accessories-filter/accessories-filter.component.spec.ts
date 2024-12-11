import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccessoriesFilterComponent } from './accessories-filter.component';

describe('AccessoriesFilterComponent', () => {
  let component: AccessoriesFilterComponent;
  let fixture: ComponentFixture<AccessoriesFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AccessoriesFilterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccessoriesFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
