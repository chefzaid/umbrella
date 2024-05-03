import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../prospect.test-samples';

import { ProspectFormService } from './prospect-form.service';

describe('Prospect Form Service', () => {
  let service: ProspectFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProspectFormService);
  });

  describe('Service methods', () => {
    describe('createProspectFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProspectFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dailyRate: expect.any(Object),
            monthlyExpenses: expect.any(Object),
            taxRate: expect.any(Object),
            expectedStartDate: expect.any(Object),
            expectedClient: expect.any(Object),
            notes: expect.any(Object),
            formula: expect.any(Object),
          }),
        );
      });

      it('passing IProspect should create a new form with FormGroup', () => {
        const formGroup = service.createProspectFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dailyRate: expect.any(Object),
            monthlyExpenses: expect.any(Object),
            taxRate: expect.any(Object),
            expectedStartDate: expect.any(Object),
            expectedClient: expect.any(Object),
            notes: expect.any(Object),
            formula: expect.any(Object),
          }),
        );
      });
    });

    describe('getProspect', () => {
      it('should return NewProspect for default Prospect initial value', () => {
        const formGroup = service.createProspectFormGroup(sampleWithNewData);

        const prospect = service.getProspect(formGroup) as any;

        expect(prospect).toMatchObject(sampleWithNewData);
      });

      it('should return NewProspect for empty Prospect initial value', () => {
        const formGroup = service.createProspectFormGroup();

        const prospect = service.getProspect(formGroup) as any;

        expect(prospect).toMatchObject({});
      });

      it('should return IProspect', () => {
        const formGroup = service.createProspectFormGroup(sampleWithRequiredData);

        const prospect = service.getProspect(formGroup) as any;

        expect(prospect).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProspect should not enable id FormControl', () => {
        const formGroup = service.createProspectFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProspect should disable id FormControl', () => {
        const formGroup = service.createProspectFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
