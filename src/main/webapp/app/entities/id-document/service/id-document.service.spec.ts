import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIdDocument } from '../id-document.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../id-document.test-samples';

import { IdDocumentService } from './id-document.service';

const requireRestSample: IIdDocument = {
  ...sampleWithRequiredData,
};

describe('IdDocument Service', () => {
  let service: IdDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: IIdDocument | IIdDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IdDocumentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a IdDocument', () => {
      const idDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(idDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IdDocument', () => {
      const idDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(idDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IdDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IdDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IdDocument', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIdDocumentToCollectionIfMissing', () => {
      it('should add a IdDocument to an empty array', () => {
        const idDocument: IIdDocument = sampleWithRequiredData;
        expectedResult = service.addIdDocumentToCollectionIfMissing([], idDocument);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(idDocument);
      });

      it('should not add a IdDocument to an array that contains it', () => {
        const idDocument: IIdDocument = sampleWithRequiredData;
        const idDocumentCollection: IIdDocument[] = [
          {
            ...idDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIdDocumentToCollectionIfMissing(idDocumentCollection, idDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IdDocument to an array that doesn't contain it", () => {
        const idDocument: IIdDocument = sampleWithRequiredData;
        const idDocumentCollection: IIdDocument[] = [sampleWithPartialData];
        expectedResult = service.addIdDocumentToCollectionIfMissing(idDocumentCollection, idDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(idDocument);
      });

      it('should add only unique IdDocument to an array', () => {
        const idDocumentArray: IIdDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const idDocumentCollection: IIdDocument[] = [sampleWithRequiredData];
        expectedResult = service.addIdDocumentToCollectionIfMissing(idDocumentCollection, ...idDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const idDocument: IIdDocument = sampleWithRequiredData;
        const idDocument2: IIdDocument = sampleWithPartialData;
        expectedResult = service.addIdDocumentToCollectionIfMissing([], idDocument, idDocument2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(idDocument);
        expect(expectedResult).toContain(idDocument2);
      });

      it('should accept null and undefined values', () => {
        const idDocument: IIdDocument = sampleWithRequiredData;
        expectedResult = service.addIdDocumentToCollectionIfMissing([], null, idDocument, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(idDocument);
      });

      it('should return initial array if no IdDocument is added', () => {
        const idDocumentCollection: IIdDocument[] = [sampleWithRequiredData];
        expectedResult = service.addIdDocumentToCollectionIfMissing(idDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(idDocumentCollection);
      });
    });

    describe('compareIdDocument', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIdDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIdDocument(entity1, entity2);
        const compareResult2 = service.compareIdDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIdDocument(entity1, entity2);
        const compareResult2 = service.compareIdDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIdDocument(entity1, entity2);
        const compareResult2 = service.compareIdDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
