import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employment-contract.test-samples';

import { EmploymentContractFormService } from './employment-contract-form.service';

describe('EmploymentContract Form Service', () => {
  let service: EmploymentContractFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmploymentContractFormService);
  });

  describe('Service methods', () => {
    describe('createEmploymentContractFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmploymentContractFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            jobTitle: expect.any(Object),
            hireDate: expect.any(Object),
            salary: expect.any(Object),
            yearlyWorkDays: expect.any(Object),
            signedByEmployer: expect.any(Object),
            signedByEmployee: expect.any(Object),
            forumula: expect.any(Object),
            employmentContract: expect.any(Object),
          }),
        );
      });

      it('passing IEmploymentContract should create a new form with FormGroup', () => {
        const formGroup = service.createEmploymentContractFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            jobTitle: expect.any(Object),
            hireDate: expect.any(Object),
            salary: expect.any(Object),
            yearlyWorkDays: expect.any(Object),
            signedByEmployer: expect.any(Object),
            signedByEmployee: expect.any(Object),
            forumula: expect.any(Object),
            employmentContract: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmploymentContract', () => {
      it('should return NewEmploymentContract for default EmploymentContract initial value', () => {
        const formGroup = service.createEmploymentContractFormGroup(sampleWithNewData);

        const employmentContract = service.getEmploymentContract(formGroup) as any;

        expect(employmentContract).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmploymentContract for empty EmploymentContract initial value', () => {
        const formGroup = service.createEmploymentContractFormGroup();

        const employmentContract = service.getEmploymentContract(formGroup) as any;

        expect(employmentContract).toMatchObject({});
      });

      it('should return IEmploymentContract', () => {
        const formGroup = service.createEmploymentContractFormGroup(sampleWithRequiredData);

        const employmentContract = service.getEmploymentContract(formGroup) as any;

        expect(employmentContract).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmploymentContract should not enable id FormControl', () => {
        const formGroup = service.createEmploymentContractFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmploymentContract should disable id FormControl', () => {
        const formGroup = service.createEmploymentContractFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
