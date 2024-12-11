import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FigurineFilterComponent } from './figurine-filter.component';

describe('FigurineFilterComponent', () => {
  let component: FigurineFilterComponent;
  let fixture: ComponentFixture<FigurineFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FigurineFilterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FigurineFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
