import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../time-sheet-line.test-samples';

import { TimeSheetLineFormService } from './time-sheet-line-form.service';

describe('TimeSheetLine Form Service', () => {
  let service: TimeSheetLineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TimeSheetLineFormService);
  });

  describe('Service methods', () => {
    describe('createTimeSheetLineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTimeSheetLineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            monthlyDays: expect.any(Object),
            extraHours: expect.any(Object),
            comments: expect.any(Object),
            timeSheet: expect.any(Object),
          }),
        );
      });

      it('passing ITimeSheetLine should create a new form with FormGroup', () => {
        const formGroup = service.createTimeSheetLineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            monthlyDays: expect.any(Object),
            extraHours: expect.any(Object),
            comments: expect.any(Object),
            timeSheet: expect.any(Object),
          }),
        );
      });
    });

    describe('getTimeSheetLine', () => {
      it('should return NewTimeSheetLine for default TimeSheetLine initial value', () => {
        const formGroup = service.createTimeSheetLineFormGroup(sampleWithNewData);

        const timeSheetLine = service.getTimeSheetLine(formGroup) as any;

        expect(timeSheetLine).toMatchObject(sampleWithNewData);
      });

      it('should return NewTimeSheetLine for empty TimeSheetLine initial value', () => {
        const formGroup = service.createTimeSheetLineFormGroup();

        const timeSheetLine = service.getTimeSheetLine(formGroup) as any;

        expect(timeSheetLine).toMatchObject({});
      });

      it('should return ITimeSheetLine', () => {
        const formGroup = service.createTimeSheetLineFormGroup(sampleWithRequiredData);

        const timeSheetLine = service.getTimeSheetLine(formGroup) as any;

        expect(timeSheetLine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITimeSheetLine should not enable id FormControl', () => {
        const formGroup = service.createTimeSheetLineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTimeSheetLine should disable id FormControl', () => {
        const formGroup = service.createTimeSheetLineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
