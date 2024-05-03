import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITimeSheetLine } from '../time-sheet-line.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../time-sheet-line.test-samples';

import { TimeSheetLineService } from './time-sheet-line.service';

const requireRestSample: ITimeSheetLine = {
  ...sampleWithRequiredData,
};

describe('TimeSheetLine Service', () => {
  let service: TimeSheetLineService;
  let httpMock: HttpTestingController;
  let expectedResult: ITimeSheetLine | ITimeSheetLine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TimeSheetLineService);
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

    it('should create a TimeSheetLine', () => {
      const timeSheetLine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(timeSheetLine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TimeSheetLine', () => {
      const timeSheetLine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(timeSheetLine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TimeSheetLine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TimeSheetLine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TimeSheetLine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTimeSheetLineToCollectionIfMissing', () => {
      it('should add a TimeSheetLine to an empty array', () => {
        const timeSheetLine: ITimeSheetLine = sampleWithRequiredData;
        expectedResult = service.addTimeSheetLineToCollectionIfMissing([], timeSheetLine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(timeSheetLine);
      });

      it('should not add a TimeSheetLine to an array that contains it', () => {
        const timeSheetLine: ITimeSheetLine = sampleWithRequiredData;
        const timeSheetLineCollection: ITimeSheetLine[] = [
          {
            ...timeSheetLine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTimeSheetLineToCollectionIfMissing(timeSheetLineCollection, timeSheetLine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TimeSheetLine to an array that doesn't contain it", () => {
        const timeSheetLine: ITimeSheetLine = sampleWithRequiredData;
        const timeSheetLineCollection: ITimeSheetLine[] = [sampleWithPartialData];
        expectedResult = service.addTimeSheetLineToCollectionIfMissing(timeSheetLineCollection, timeSheetLine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(timeSheetLine);
      });

      it('should add only unique TimeSheetLine to an array', () => {
        const timeSheetLineArray: ITimeSheetLine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const timeSheetLineCollection: ITimeSheetLine[] = [sampleWithRequiredData];
        expectedResult = service.addTimeSheetLineToCollectionIfMissing(timeSheetLineCollection, ...timeSheetLineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const timeSheetLine: ITimeSheetLine = sampleWithRequiredData;
        const timeSheetLine2: ITimeSheetLine = sampleWithPartialData;
        expectedResult = service.addTimeSheetLineToCollectionIfMissing([], timeSheetLine, timeSheetLine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(timeSheetLine);
        expect(expectedResult).toContain(timeSheetLine2);
      });

      it('should accept null and undefined values', () => {
        const timeSheetLine: ITimeSheetLine = sampleWithRequiredData;
        expectedResult = service.addTimeSheetLineToCollectionIfMissing([], null, timeSheetLine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(timeSheetLine);
      });

      it('should return initial array if no TimeSheetLine is added', () => {
        const timeSheetLineCollection: ITimeSheetLine[] = [sampleWithRequiredData];
        expectedResult = service.addTimeSheetLineToCollectionIfMissing(timeSheetLineCollection, undefined, null);
        expect(expectedResult).toEqual(timeSheetLineCollection);
      });
    });

    describe('compareTimeSheetLine', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTimeSheetLine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTimeSheetLine(entity1, entity2);
        const compareResult2 = service.compareTimeSheetLine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTimeSheetLine(entity1, entity2);
        const compareResult2 = service.compareTimeSheetLine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTimeSheetLine(entity1, entity2);
        const compareResult2 = service.compareTimeSheetLine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
