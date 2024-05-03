import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MileageAllowanceDetailComponent } from './mileage-allowance-detail.component';

describe('MileageAllowance Management Detail Component', () => {
  let comp: MileageAllowanceDetailComponent;
  let fixture: ComponentFixture<MileageAllowanceDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MileageAllowanceDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MileageAllowanceDetailComponent,
              resolve: { mileageAllowance: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MileageAllowanceDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MileageAllowanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mileageAllowance on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MileageAllowanceDetailComponent);

      // THEN
      expect(instance.mileageAllowance()).toEqual(expect.objectContaining({ id: 123 }));
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
