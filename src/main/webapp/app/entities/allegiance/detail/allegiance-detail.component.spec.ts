import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AllegianceDetailComponent } from './allegiance-detail.component';

describe('Allegiance Management Detail Component', () => {
  let comp: AllegianceDetailComponent;
  let fixture: ComponentFixture<AllegianceDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AllegianceDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./allegiance-detail.component').then(m => m.AllegianceDetailComponent),
              resolve: { allegiance: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AllegianceDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllegianceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load allegiance on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AllegianceDetailComponent);

      // THEN
      expect(instance.allegiance()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
