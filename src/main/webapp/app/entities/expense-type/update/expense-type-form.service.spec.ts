import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../expense-type.test-samples';

import { ExpenseTypeFormService } from './expense-type-form.service';

describe('ExpenseType Form Service', () => {
  let service: ExpenseTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpenseTypeFormService);
  });

  describe('Service methods', () => {
    describe('createExpenseTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExpenseTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            description: expect.any(Object),
            reimbursmentPct: expect.any(Object),
          }),
        );
      });

      it('passing IExpenseType should create a new form with FormGroup', () => {
        const formGroup = service.createExpenseTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            description: expect.any(Object),
            reimbursmentPct: expect.any(Object),
          }),
        );
      });
    });

    describe('getExpenseType', () => {
      it('should return NewExpenseType for default ExpenseType initial value', () => {
        const formGroup = service.createExpenseTypeFormGroup(sampleWithNewData);

        const expenseType = service.getExpenseType(formGroup) as any;

        expect(expenseType).toMatchObject(sampleWithNewData);
      });

      it('should return NewExpenseType for empty ExpenseType initial value', () => {
        const formGroup = service.createExpenseTypeFormGroup();

        const expenseType = service.getExpenseType(formGroup) as any;

        expect(expenseType).toMatchObject({});
      });

      it('should return IExpenseType', () => {
        const formGroup = service.createExpenseTypeFormGroup(sampleWithRequiredData);

        const expenseType = service.getExpenseType(formGroup) as any;

        expect(expenseType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExpenseType should not enable id FormControl', () => {
        const formGroup = service.createExpenseTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExpenseType should disable id FormControl', () => {
        const formGroup = service.createExpenseTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
