import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pay-slip.test-samples';

import { PaySlipFormService } from './pay-slip-form.service';

describe('PaySlip Form Service', () => {
  let service: PaySlipFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaySlipFormService);
  });

  describe('Service methods', () => {
    describe('createPaySlipFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaySlipFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            superGrossSalary: expect.any(Object),
            grossSalary: expect.any(Object),
            netSalary: expect.any(Object),
            taxRate: expect.any(Object),
            amountPaid: expect.any(Object),
            totalExpenses: expect.any(Object),
            document: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });

      it('passing IPaySlip should create a new form with FormGroup', () => {
        const formGroup = service.createPaySlipFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            superGrossSalary: expect.any(Object),
            grossSalary: expect.any(Object),
            netSalary: expect.any(Object),
            taxRate: expect.any(Object),
            amountPaid: expect.any(Object),
            totalExpenses: expect.any(Object),
            document: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });
    });

    describe('getPaySlip', () => {
      it('should return NewPaySlip for default PaySlip initial value', () => {
        const formGroup = service.createPaySlipFormGroup(sampleWithNewData);

        const paySlip = service.getPaySlip(formGroup) as any;

        expect(paySlip).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaySlip for empty PaySlip initial value', () => {
        const formGroup = service.createPaySlipFormGroup();

        const paySlip = service.getPaySlip(formGroup) as any;

        expect(paySlip).toMatchObject({});
      });

      it('should return IPaySlip', () => {
        const formGroup = service.createPaySlipFormGroup(sampleWithRequiredData);

        const paySlip = service.getPaySlip(formGroup) as any;

        expect(paySlip).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaySlip should not enable id FormControl', () => {
        const formGroup = service.createPaySlipFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaySlip should disable id FormControl', () => {
        const formGroup = service.createPaySlipFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
