import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IParameterGroup } from '../parameter-group.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../parameter-group.test-samples';

import { ParameterGroupService } from './parameter-group.service';

const requireRestSample: IParameterGroup = {
  ...sampleWithRequiredData,
};

describe('ParameterGroup Service', () => {
  let service: ParameterGroupService;
  let httpMock: HttpTestingController;
  let expectedResult: IParameterGroup | IParameterGroup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ParameterGroupService);
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

    it('should create a ParameterGroup', () => {
      const parameterGroup = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(parameterGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ParameterGroup', () => {
      const parameterGroup = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(parameterGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ParameterGroup', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ParameterGroup', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ParameterGroup', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addParameterGroupToCollectionIfMissing', () => {
      it('should add a ParameterGroup to an empty array', () => {
        const parameterGroup: IParameterGroup = sampleWithRequiredData;
        expectedResult = service.addParameterGroupToCollectionIfMissing([], parameterGroup);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parameterGroup);
      });

      it('should not add a ParameterGroup to an array that contains it', () => {
        const parameterGroup: IParameterGroup = sampleWithRequiredData;
        const parameterGroupCollection: IParameterGroup[] = [
          {
            ...parameterGroup,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addParameterGroupToCollectionIfMissing(parameterGroupCollection, parameterGroup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ParameterGroup to an array that doesn't contain it", () => {
        const parameterGroup: IParameterGroup = sampleWithRequiredData;
        const parameterGroupCollection: IParameterGroup[] = [sampleWithPartialData];
        expectedResult = service.addParameterGroupToCollectionIfMissing(parameterGroupCollection, parameterGroup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parameterGroup);
      });

      it('should add only unique ParameterGroup to an array', () => {
        const parameterGroupArray: IParameterGroup[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const parameterGroupCollection: IParameterGroup[] = [sampleWithRequiredData];
        expectedResult = service.addParameterGroupToCollectionIfMissing(parameterGroupCollection, ...parameterGroupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const parameterGroup: IParameterGroup = sampleWithRequiredData;
        const parameterGroup2: IParameterGroup = sampleWithPartialData;
        expectedResult = service.addParameterGroupToCollectionIfMissing([], parameterGroup, parameterGroup2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parameterGroup);
        expect(expectedResult).toContain(parameterGroup2);
      });

      it('should accept null and undefined values', () => {
        const parameterGroup: IParameterGroup = sampleWithRequiredData;
        expectedResult = service.addParameterGroupToCollectionIfMissing([], null, parameterGroup, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parameterGroup);
      });

      it('should return initial array if no ParameterGroup is added', () => {
        const parameterGroupCollection: IParameterGroup[] = [sampleWithRequiredData];
        expectedResult = service.addParameterGroupToCollectionIfMissing(parameterGroupCollection, undefined, null);
        expect(expectedResult).toEqual(parameterGroupCollection);
      });
    });

    describe('compareParameterGroup', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareParameterGroup(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareParameterGroup(entity1, entity2);
        const compareResult2 = service.compareParameterGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareParameterGroup(entity1, entity2);
        const compareResult2 = service.compareParameterGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareParameterGroup(entity1, entity2);
        const compareResult2 = service.compareParameterGroup(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
