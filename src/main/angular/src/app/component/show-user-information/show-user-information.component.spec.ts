import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowUserInformationComponent } from './show-user-information.component';

describe('ShowUserInformationComponent', () => {
  let component: ShowUserInformationComponent;
  let fixture: ComponentFixture<ShowUserInformationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShowUserInformationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowUserInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
