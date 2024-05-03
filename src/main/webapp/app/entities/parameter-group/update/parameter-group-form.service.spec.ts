import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../parameter-group.test-samples';

import { ParameterGroupFormService } from './parameter-group-form.service';

describe('ParameterGroup Form Service', () => {
  let service: ParameterGroupFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParameterGroupFormService);
  });

  describe('Service methods', () => {
    describe('createParameterGroupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createParameterGroupFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IParameterGroup should create a new form with FormGroup', () => {
        const formGroup = service.createParameterGroupFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getParameterGroup', () => {
      it('should return NewParameterGroup for default ParameterGroup initial value', () => {
        const formGroup = service.createParameterGroupFormGroup(sampleWithNewData);

        const parameterGroup = service.getParameterGroup(formGroup) as any;

        expect(parameterGroup).toMatchObject(sampleWithNewData);
      });

      it('should return NewParameterGroup for empty ParameterGroup initial value', () => {
        const formGroup = service.createParameterGroupFormGroup();

        const parameterGroup = service.getParameterGroup(formGroup) as any;

        expect(parameterGroup).toMatchObject({});
      });

      it('should return IParameterGroup', () => {
        const formGroup = service.createParameterGroupFormGroup(sampleWithRequiredData);

        const parameterGroup = service.getParameterGroup(formGroup) as any;

        expect(parameterGroup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParameterGroup should not enable id FormControl', () => {
        const formGroup = service.createParameterGroupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParameterGroup should disable id FormControl', () => {
        const formGroup = service.createParameterGroupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
