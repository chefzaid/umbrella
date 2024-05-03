import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../service-contract.test-samples';

import { ServiceContractFormService } from './service-contract-form.service';

describe('ServiceContract Form Service', () => {
  let service: ServiceContractFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceContractFormService);
  });

  describe('Service methods', () => {
    describe('createServiceContractFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServiceContractFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceLabel: expect.any(Object),
            dailyRate: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            extensionTerms: expect.any(Object),
            signedBySupplier: expect.any(Object),
            signedByClient: expect.any(Object),
            employee: expect.any(Object),
            serviceContract: expect.any(Object),
          }),
        );
      });

      it('passing IServiceContract should create a new form with FormGroup', () => {
        const formGroup = service.createServiceContractFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceLabel: expect.any(Object),
            dailyRate: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            extensionTerms: expect.any(Object),
            signedBySupplier: expect.any(Object),
            signedByClient: expect.any(Object),
            employee: expect.any(Object),
            serviceContract: expect.any(Object),
          }),
        );
      });
    });

    describe('getServiceContract', () => {
      it('should return NewServiceContract for default ServiceContract initial value', () => {
        const formGroup = service.createServiceContractFormGroup(sampleWithNewData);

        const serviceContract = service.getServiceContract(formGroup) as any;

        expect(serviceContract).toMatchObject(sampleWithNewData);
      });

      it('should return NewServiceContract for empty ServiceContract initial value', () => {
        const formGroup = service.createServiceContractFormGroup();

        const serviceContract = service.getServiceContract(formGroup) as any;

        expect(serviceContract).toMatchObject({});
      });

      it('should return IServiceContract', () => {
        const formGroup = service.createServiceContractFormGroup(sampleWithRequiredData);

        const serviceContract = service.getServiceContract(formGroup) as any;

        expect(serviceContract).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServiceContract should not enable id FormControl', () => {
        const formGroup = service.createServiceContractFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServiceContract should disable id FormControl', () => {
        const formGroup = service.createServiceContractFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
