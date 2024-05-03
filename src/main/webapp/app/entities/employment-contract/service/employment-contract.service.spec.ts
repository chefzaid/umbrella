import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEmploymentContract } from '../employment-contract.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../employment-contract.test-samples';

import { EmploymentContractService, RestEmploymentContract } from './employment-contract.service';

const requireRestSample: RestEmploymentContract = {
  ...sampleWithRequiredData,
  hireDate: sampleWithRequiredData.hireDate?.format(DATE_FORMAT),
};

describe('EmploymentContract Service', () => {
  let service: EmploymentContractService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmploymentContract | IEmploymentContract[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmploymentContractService);
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

    it('should create a EmploymentContract', () => {
      const employmentContract = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employmentContract).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmploymentContract', () => {
      const employmentContract = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employmentContract).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmploymentContract', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmploymentContract', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmploymentContract', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmploymentContractToCollectionIfMissing', () => {
      it('should add a EmploymentContract to an empty array', () => {
        const employmentContract: IEmploymentContract = sampleWithRequiredData;
        expectedResult = service.addEmploymentContractToCollectionIfMissing([], employmentContract);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employmentContract);
      });

      it('should not add a EmploymentContract to an array that contains it', () => {
        const employmentContract: IEmploymentContract = sampleWithRequiredData;
        const employmentContractCollection: IEmploymentContract[] = [
          {
            ...employmentContract,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmploymentContractToCollectionIfMissing(employmentContractCollection, employmentContract);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmploymentContract to an array that doesn't contain it", () => {
        const employmentContract: IEmploymentContract = sampleWithRequiredData;
        const employmentContractCollection: IEmploymentContract[] = [sampleWithPartialData];
        expectedResult = service.addEmploymentContractToCollectionIfMissing(employmentContractCollection, employmentContract);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employmentContract);
      });

      it('should add only unique EmploymentContract to an array', () => {
        const employmentContractArray: IEmploymentContract[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employmentContractCollection: IEmploymentContract[] = [sampleWithRequiredData];
        expectedResult = service.addEmploymentContractToCollectionIfMissing(employmentContractCollection, ...employmentContractArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employmentContract: IEmploymentContract = sampleWithRequiredData;
        const employmentContract2: IEmploymentContract = sampleWithPartialData;
        expectedResult = service.addEmploymentContractToCollectionIfMissing([], employmentContract, employmentContract2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employmentContract);
        expect(expectedResult).toContain(employmentContract2);
      });

      it('should accept null and undefined values', () => {
        const employmentContract: IEmploymentContract = sampleWithRequiredData;
        expectedResult = service.addEmploymentContractToCollectionIfMissing([], null, employmentContract, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employmentContract);
      });

      it('should return initial array if no EmploymentContract is added', () => {
        const employmentContractCollection: IEmploymentContract[] = [sampleWithRequiredData];
        expectedResult = service.addEmploymentContractToCollectionIfMissing(employmentContractCollection, undefined, null);
        expect(expectedResult).toEqual(employmentContractCollection);
      });
    });

    describe('compareEmploymentContract', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmploymentContract(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmploymentContract(entity1, entity2);
        const compareResult2 = service.compareEmploymentContract(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmploymentContract(entity1, entity2);
        const compareResult2 = service.compareEmploymentContract(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmploymentContract(entity1, entity2);
        const compareResult2 = service.compareEmploymentContract(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
