import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ActivityReportDetailComponent } from './activity-report-detail.component';

describe('ActivityReport Management Detail Component', () => {
  let comp: ActivityReportDetailComponent;
  let fixture: ComponentFixture<ActivityReportDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActivityReportDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ActivityReportDetailComponent,
              resolve: { activityReport: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ActivityReportDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivityReportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load activityReport on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ActivityReportDetailComponent);

      // THEN
      expect(instance.activityReport()).toEqual(expect.objectContaining({ id: 123 }));
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
