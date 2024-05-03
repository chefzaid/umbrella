import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../mileage-allowance.test-samples';

import { MileageAllowanceFormService } from './mileage-allowance-form.service';

describe('MileageAllowance Form Service', () => {
  let service: MileageAllowanceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MileageAllowanceFormService);
  });

  describe('Service methods', () => {
    describe('createMileageAllowanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMileageAllowanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mileage: expect.any(Object),
            multiplier: expect.any(Object),
          }),
        );
      });

      it('passing IMileageAllowance should create a new form with FormGroup', () => {
        const formGroup = service.createMileageAllowanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mileage: expect.any(Object),
            multiplier: expect.any(Object),
          }),
        );
      });
    });

    describe('getMileageAllowance', () => {
      it('should return NewMileageAllowance for default MileageAllowance initial value', () => {
        const formGroup = service.createMileageAllowanceFormGroup(sampleWithNewData);

        const mileageAllowance = service.getMileageAllowance(formGroup) as any;

        expect(mileageAllowance).toMatchObject(sampleWithNewData);
      });

      it('should return NewMileageAllowance for empty MileageAllowance initial value', () => {
        const formGroup = service.createMileageAllowanceFormGroup();

        const mileageAllowance = service.getMileageAllowance(formGroup) as any;

        expect(mileageAllowance).toMatchObject({});
      });

      it('should return IMileageAllowance', () => {
        const formGroup = service.createMileageAllowanceFormGroup(sampleWithRequiredData);

        const mileageAllowance = service.getMileageAllowance(formGroup) as any;

        expect(mileageAllowance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMileageAllowance should not enable id FormControl', () => {
        const formGroup = service.createMileageAllowanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMileageAllowance should disable id FormControl', () => {
        const formGroup = service.createMileageAllowanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
