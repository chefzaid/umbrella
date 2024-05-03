import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IServiceContract } from '../service-contract.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../service-contract.test-samples';

import { ServiceContractService, RestServiceContract } from './service-contract.service';

const requireRestSample: RestServiceContract = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('ServiceContract Service', () => {
  let service: ServiceContractService;
  let httpMock: HttpTestingController;
  let expectedResult: IServiceContract | IServiceContract[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceContractService);
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

    it('should create a ServiceContract', () => {
      const serviceContract = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(serviceContract).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceContract', () => {
      const serviceContract = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(serviceContract).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceContract', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceContract', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServiceContract', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addServiceContractToCollectionIfMissing', () => {
      it('should add a ServiceContract to an empty array', () => {
        const serviceContract: IServiceContract = sampleWithRequiredData;
        expectedResult = service.addServiceContractToCollectionIfMissing([], serviceContract);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceContract);
      });

      it('should not add a ServiceContract to an array that contains it', () => {
        const serviceContract: IServiceContract = sampleWithRequiredData;
        const serviceContractCollection: IServiceContract[] = [
          {
            ...serviceContract,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServiceContractToCollectionIfMissing(serviceContractCollection, serviceContract);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceContract to an array that doesn't contain it", () => {
        const serviceContract: IServiceContract = sampleWithRequiredData;
        const serviceContractCollection: IServiceContract[] = [sampleWithPartialData];
        expectedResult = service.addServiceContractToCollectionIfMissing(serviceContractCollection, serviceContract);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceContract);
      });

      it('should add only unique ServiceContract to an array', () => {
        const serviceContractArray: IServiceContract[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const serviceContractCollection: IServiceContract[] = [sampleWithRequiredData];
        expectedResult = service.addServiceContractToCollectionIfMissing(serviceContractCollection, ...serviceContractArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceContract: IServiceContract = sampleWithRequiredData;
        const serviceContract2: IServiceContract = sampleWithPartialData;
        expectedResult = service.addServiceContractToCollectionIfMissing([], serviceContract, serviceContract2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceContract);
        expect(expectedResult).toContain(serviceContract2);
      });

      it('should accept null and undefined values', () => {
        const serviceContract: IServiceContract = sampleWithRequiredData;
        expectedResult = service.addServiceContractToCollectionIfMissing([], null, serviceContract, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceContract);
      });

      it('should return initial array if no ServiceContract is added', () => {
        const serviceContractCollection: IServiceContract[] = [sampleWithRequiredData];
        expectedResult = service.addServiceContractToCollectionIfMissing(serviceContractCollection, undefined, null);
        expect(expectedResult).toEqual(serviceContractCollection);
      });
    });

    describe('compareServiceContract', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServiceContract(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareServiceContract(entity1, entity2);
        const compareResult2 = service.compareServiceContract(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareServiceContract(entity1, entity2);
        const compareResult2 = service.compareServiceContract(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareServiceContract(entity1, entity2);
        const compareResult2 = service.compareServiceContract(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
