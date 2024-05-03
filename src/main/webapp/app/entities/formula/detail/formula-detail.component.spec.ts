import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FormulaDetailComponent } from './formula-detail.component';

describe('Formula Management Detail Component', () => {
  let comp: FormulaDetailComponent;
  let fixture: ComponentFixture<FormulaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormulaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FormulaDetailComponent,
              resolve: { formula: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FormulaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormulaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load formula on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FormulaDetailComponent);

      // THEN
      expect(instance.formula()).toEqual(expect.objectContaining({ id: 123 }));
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
