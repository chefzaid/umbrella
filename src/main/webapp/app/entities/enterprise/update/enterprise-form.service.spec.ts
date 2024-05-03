import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../enterprise.test-samples';

import { EnterpriseFormService } from './enterprise-form.service';

describe('Enterprise Form Service', () => {
  let service: EnterpriseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EnterpriseFormService);
  });

  describe('Service methods', () => {
    describe('createEnterpriseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEnterpriseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            companyStatus: expect.any(Object),
            logo: expect.any(Object),
            siret: expect.any(Object),
            siren: expect.any(Object),
            salesTaxNumber: expect.any(Object),
            iban: expect.any(Object),
            bicSwift: expect.any(Object),
            website: expect.any(Object),
            defaultInvoiceTerms: expect.any(Object),
          }),
        );
      });

      it('passing IEnterprise should create a new form with FormGroup', () => {
        const formGroup = service.createEnterpriseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            companyStatus: expect.any(Object),
            logo: expect.any(Object),
            siret: expect.any(Object),
            siren: expect.any(Object),
            salesTaxNumber: expect.any(Object),
            iban: expect.any(Object),
            bicSwift: expect.any(Object),
            website: expect.any(Object),
            defaultInvoiceTerms: expect.any(Object),
          }),
        );
      });
    });

    describe('getEnterprise', () => {
      it('should return NewEnterprise for default Enterprise initial value', () => {
        const formGroup = service.createEnterpriseFormGroup(sampleWithNewData);

        const enterprise = service.getEnterprise(formGroup) as any;

        expect(enterprise).toMatchObject(sampleWithNewData);
      });

      it('should return NewEnterprise for empty Enterprise initial value', () => {
        const formGroup = service.createEnterpriseFormGroup();

        const enterprise = service.getEnterprise(formGroup) as any;

        expect(enterprise).toMatchObject({});
      });

      it('should return IEnterprise', () => {
        const formGroup = service.createEnterpriseFormGroup(sampleWithRequiredData);

        const enterprise = service.getEnterprise(formGroup) as any;

        expect(enterprise).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEnterprise should not enable id FormControl', () => {
        const formGroup = service.createEnterpriseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEnterprise should disable id FormControl', () => {
        const formGroup = service.createEnterpriseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
