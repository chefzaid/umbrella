import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../id-document.test-samples';

import { IdDocumentFormService } from './id-document-form.service';

describe('IdDocument Form Service', () => {
  let service: IdDocumentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IdDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createIdDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIdDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idType: expect.any(Object),
            idNumber: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });

      it('passing IIdDocument should create a new form with FormGroup', () => {
        const formGroup = service.createIdDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idType: expect.any(Object),
            idNumber: expect.any(Object),
            document: expect.any(Object),
          }),
        );
      });
    });

    describe('getIdDocument', () => {
      it('should return NewIdDocument for default IdDocument initial value', () => {
        const formGroup = service.createIdDocumentFormGroup(sampleWithNewData);

        const idDocument = service.getIdDocument(formGroup) as any;

        expect(idDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewIdDocument for empty IdDocument initial value', () => {
        const formGroup = service.createIdDocumentFormGroup();

        const idDocument = service.getIdDocument(formGroup) as any;

        expect(idDocument).toMatchObject({});
      });

      it('should return IIdDocument', () => {
        const formGroup = service.createIdDocumentFormGroup(sampleWithRequiredData);

        const idDocument = service.getIdDocument(formGroup) as any;

        expect(idDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIdDocument should not enable id FormControl', () => {
        const formGroup = service.createIdDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIdDocument should disable id FormControl', () => {
        const formGroup = service.createIdDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
