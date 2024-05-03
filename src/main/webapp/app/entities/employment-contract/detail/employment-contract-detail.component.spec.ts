import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EmploymentContractDetailComponent } from './employment-contract-detail.component';

describe('EmploymentContract Management Detail Component', () => {
  let comp: EmploymentContractDetailComponent;
  let fixture: ComponentFixture<EmploymentContractDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmploymentContractDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EmploymentContractDetailComponent,
              resolve: { employmentContract: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EmploymentContractDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmploymentContractDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employmentContract on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EmploymentContractDetailComponent);

      // THEN
      expect(instance.employmentContract()).toEqual(expect.objectContaining({ id: 123 }));
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
