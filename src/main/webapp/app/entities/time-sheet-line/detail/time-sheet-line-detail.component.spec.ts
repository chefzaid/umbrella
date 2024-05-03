import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TimeSheetLineDetailComponent } from './time-sheet-line-detail.component';

describe('TimeSheetLine Management Detail Component', () => {
  let comp: TimeSheetLineDetailComponent;
  let fixture: ComponentFixture<TimeSheetLineDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TimeSheetLineDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TimeSheetLineDetailComponent,
              resolve: { timeSheetLine: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TimeSheetLineDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimeSheetLineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load timeSheetLine on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TimeSheetLineDetailComponent);

      // THEN
      expect(instance.timeSheetLine()).toEqual(expect.objectContaining({ id: 123 }));
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
