import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../expense-note.test-samples';

import { ExpenseNoteFormService } from './expense-note-form.service';

describe('ExpenseNote Form Service', () => {
  let service: ExpenseNoteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpenseNoteFormService);
  });

  describe('Service methods', () => {
    describe('createExpenseNoteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExpenseNoteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            concernedMonth: expect.any(Object),
            creationDate: expect.any(Object),
            submitDate: expect.any(Object),
            validationDate: expect.any(Object),
            mileageAllowance: expect.any(Object),
            document: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });

      it('passing IExpenseNote should create a new form with FormGroup', () => {
        const formGroup = service.createExpenseNoteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            concernedMonth: expect.any(Object),
            creationDate: expect.any(Object),
            submitDate: expect.any(Object),
            validationDate: expect.any(Object),
            mileageAllowance: expect.any(Object),
            document: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });
    });

    describe('getExpenseNote', () => {
      it('should return NewExpenseNote for default ExpenseNote initial value', () => {
        const formGroup = service.createExpenseNoteFormGroup(sampleWithNewData);

        const expenseNote = service.getExpenseNote(formGroup) as any;

        expect(expenseNote).toMatchObject(sampleWithNewData);
      });

      it('should return NewExpenseNote for empty ExpenseNote initial value', () => {
        const formGroup = service.createExpenseNoteFormGroup();

        const expenseNote = service.getExpenseNote(formGroup) as any;

        expect(expenseNote).toMatchObject({});
      });

      it('should return IExpenseNote', () => {
        const formGroup = service.createExpenseNoteFormGroup(sampleWithRequiredData);

        const expenseNote = service.getExpenseNote(formGroup) as any;

        expect(expenseNote).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExpenseNote should not enable id FormControl', () => {
        const formGroup = service.createExpenseNoteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExpenseNote should disable id FormControl', () => {
        const formGroup = service.createExpenseNoteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
