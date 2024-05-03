import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../activity-report.test-samples';

import { ActivityReportFormService } from './activity-report-form.service';

describe('ActivityReport Form Service', () => {
  let service: ActivityReportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ActivityReportFormService);
  });

  describe('Service methods', () => {
    describe('createActivityReportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createActivityReportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            balance: expect.any(Object),
            comments: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });

      it('passing IActivityReport should create a new form with FormGroup', () => {
        const formGroup = service.createActivityReportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            balance: expect.any(Object),
            comments: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });
    });

    describe('getActivityReport', () => {
      it('should return NewActivityReport for default ActivityReport initial value', () => {
        const formGroup = service.createActivityReportFormGroup(sampleWithNewData);

        const activityReport = service.getActivityReport(formGroup) as any;

        expect(activityReport).toMatchObject(sampleWithNewData);
      });

      it('should return NewActivityReport for empty ActivityReport initial value', () => {
        const formGroup = service.createActivityReportFormGroup();

        const activityReport = service.getActivityReport(formGroup) as any;

        expect(activityReport).toMatchObject({});
      });

      it('should return IActivityReport', () => {
        const formGroup = service.createActivityReportFormGroup(sampleWithRequiredData);

        const activityReport = service.getActivityReport(formGroup) as any;

        expect(activityReport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IActivityReport should not enable id FormControl', () => {
        const formGroup = service.createActivityReportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewActivityReport should disable id FormControl', () => {
        const formGroup = service.createActivityReportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
