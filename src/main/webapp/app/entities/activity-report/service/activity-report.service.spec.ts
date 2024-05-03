import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IActivityReport } from '../activity-report.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../activity-report.test-samples';

import { ActivityReportService, RestActivityReport } from './activity-report.service';

const requireRestSample: RestActivityReport = {
  ...sampleWithRequiredData,
  month: sampleWithRequiredData.month?.format(DATE_FORMAT),
};

describe('ActivityReport Service', () => {
  let service: ActivityReportService;
  let httpMock: HttpTestingController;
  let expectedResult: IActivityReport | IActivityReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ActivityReportService);
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

    it('should create a ActivityReport', () => {
      const activityReport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(activityReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ActivityReport', () => {
      const activityReport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(activityReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ActivityReport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ActivityReport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ActivityReport', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addActivityReportToCollectionIfMissing', () => {
      it('should add a ActivityReport to an empty array', () => {
        const activityReport: IActivityReport = sampleWithRequiredData;
        expectedResult = service.addActivityReportToCollectionIfMissing([], activityReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(activityReport);
      });

      it('should not add a ActivityReport to an array that contains it', () => {
        const activityReport: IActivityReport = sampleWithRequiredData;
        const activityReportCollection: IActivityReport[] = [
          {
            ...activityReport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addActivityReportToCollectionIfMissing(activityReportCollection, activityReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ActivityReport to an array that doesn't contain it", () => {
        const activityReport: IActivityReport = sampleWithRequiredData;
        const activityReportCollection: IActivityReport[] = [sampleWithPartialData];
        expectedResult = service.addActivityReportToCollectionIfMissing(activityReportCollection, activityReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(activityReport);
      });

      it('should add only unique ActivityReport to an array', () => {
        const activityReportArray: IActivityReport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const activityReportCollection: IActivityReport[] = [sampleWithRequiredData];
        expectedResult = service.addActivityReportToCollectionIfMissing(activityReportCollection, ...activityReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const activityReport: IActivityReport = sampleWithRequiredData;
        const activityReport2: IActivityReport = sampleWithPartialData;
        expectedResult = service.addActivityReportToCollectionIfMissing([], activityReport, activityReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(activityReport);
        expect(expectedResult).toContain(activityReport2);
      });

      it('should accept null and undefined values', () => {
        const activityReport: IActivityReport = sampleWithRequiredData;
        expectedResult = service.addActivityReportToCollectionIfMissing([], null, activityReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(activityReport);
      });

      it('should return initial array if no ActivityReport is added', () => {
        const activityReportCollection: IActivityReport[] = [sampleWithRequiredData];
        expectedResult = service.addActivityReportToCollectionIfMissing(activityReportCollection, undefined, null);
        expect(expectedResult).toEqual(activityReportCollection);
      });
    });

    describe('compareActivityReport', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareActivityReport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareActivityReport(entity1, entity2);
        const compareResult2 = service.compareActivityReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareActivityReport(entity1, entity2);
        const compareResult2 = service.compareActivityReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareActivityReport(entity1, entity2);
        const compareResult2 = service.compareActivityReport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
