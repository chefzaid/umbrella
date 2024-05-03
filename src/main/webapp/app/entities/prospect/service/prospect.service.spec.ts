import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProspect } from '../prospect.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../prospect.test-samples';

import { ProspectService, RestProspect } from './prospect.service';

const requireRestSample: RestProspect = {
  ...sampleWithRequiredData,
  expectedStartDate: sampleWithRequiredData.expectedStartDate?.format(DATE_FORMAT),
};

describe('Prospect Service', () => {
  let service: ProspectService;
  let httpMock: HttpTestingController;
  let expectedResult: IProspect | IProspect[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProspectService);
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

    it('should create a Prospect', () => {
      const prospect = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(prospect).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Prospect', () => {
      const prospect = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(prospect).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Prospect', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Prospect', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Prospect', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProspectToCollectionIfMissing', () => {
      it('should add a Prospect to an empty array', () => {
        const prospect: IProspect = sampleWithRequiredData;
        expectedResult = service.addProspectToCollectionIfMissing([], prospect);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prospect);
      });

      it('should not add a Prospect to an array that contains it', () => {
        const prospect: IProspect = sampleWithRequiredData;
        const prospectCollection: IProspect[] = [
          {
            ...prospect,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProspectToCollectionIfMissing(prospectCollection, prospect);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Prospect to an array that doesn't contain it", () => {
        const prospect: IProspect = sampleWithRequiredData;
        const prospectCollection: IProspect[] = [sampleWithPartialData];
        expectedResult = service.addProspectToCollectionIfMissing(prospectCollection, prospect);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prospect);
      });

      it('should add only unique Prospect to an array', () => {
        const prospectArray: IProspect[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const prospectCollection: IProspect[] = [sampleWithRequiredData];
        expectedResult = service.addProspectToCollectionIfMissing(prospectCollection, ...prospectArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prospect: IProspect = sampleWithRequiredData;
        const prospect2: IProspect = sampleWithPartialData;
        expectedResult = service.addProspectToCollectionIfMissing([], prospect, prospect2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prospect);
        expect(expectedResult).toContain(prospect2);
      });

      it('should accept null and undefined values', () => {
        const prospect: IProspect = sampleWithRequiredData;
        expectedResult = service.addProspectToCollectionIfMissing([], null, prospect, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prospect);
      });

      it('should return initial array if no Prospect is added', () => {
        const prospectCollection: IProspect[] = [sampleWithRequiredData];
        expectedResult = service.addProspectToCollectionIfMissing(prospectCollection, undefined, null);
        expect(expectedResult).toEqual(prospectCollection);
      });
    });

    describe('compareProspect', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProspect(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProspect(entity1, entity2);
        const compareResult2 = service.compareProspect(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProspect(entity1, entity2);
        const compareResult2 = service.compareProspect(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProspect(entity1, entity2);
        const compareResult2 = service.compareProspect(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
