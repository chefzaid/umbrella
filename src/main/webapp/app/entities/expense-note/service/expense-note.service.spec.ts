import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IExpenseNote } from '../expense-note.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../expense-note.test-samples';

import { ExpenseNoteService, RestExpenseNote } from './expense-note.service';

const requireRestSample: RestExpenseNote = {
  ...sampleWithRequiredData,
  creationDate: sampleWithRequiredData.creationDate?.format(DATE_FORMAT),
  submitDate: sampleWithRequiredData.submitDate?.toJSON(),
  validationDate: sampleWithRequiredData.validationDate?.toJSON(),
};

describe('ExpenseNote Service', () => {
  let service: ExpenseNoteService;
  let httpMock: HttpTestingController;
  let expectedResult: IExpenseNote | IExpenseNote[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExpenseNoteService);
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

    it('should create a ExpenseNote', () => {
      const expenseNote = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(expenseNote).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExpenseNote', () => {
      const expenseNote = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(expenseNote).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExpenseNote', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExpenseNote', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExpenseNote', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExpenseNoteToCollectionIfMissing', () => {
      it('should add a ExpenseNote to an empty array', () => {
        const expenseNote: IExpenseNote = sampleWithRequiredData;
        expectedResult = service.addExpenseNoteToCollectionIfMissing([], expenseNote);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseNote);
      });

      it('should not add a ExpenseNote to an array that contains it', () => {
        const expenseNote: IExpenseNote = sampleWithRequiredData;
        const expenseNoteCollection: IExpenseNote[] = [
          {
            ...expenseNote,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExpenseNoteToCollectionIfMissing(expenseNoteCollection, expenseNote);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExpenseNote to an array that doesn't contain it", () => {
        const expenseNote: IExpenseNote = sampleWithRequiredData;
        const expenseNoteCollection: IExpenseNote[] = [sampleWithPartialData];
        expectedResult = service.addExpenseNoteToCollectionIfMissing(expenseNoteCollection, expenseNote);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseNote);
      });

      it('should add only unique ExpenseNote to an array', () => {
        const expenseNoteArray: IExpenseNote[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const expenseNoteCollection: IExpenseNote[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseNoteToCollectionIfMissing(expenseNoteCollection, ...expenseNoteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const expenseNote: IExpenseNote = sampleWithRequiredData;
        const expenseNote2: IExpenseNote = sampleWithPartialData;
        expectedResult = service.addExpenseNoteToCollectionIfMissing([], expenseNote, expenseNote2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expenseNote);
        expect(expectedResult).toContain(expenseNote2);
      });

      it('should accept null and undefined values', () => {
        const expenseNote: IExpenseNote = sampleWithRequiredData;
        expectedResult = service.addExpenseNoteToCollectionIfMissing([], null, expenseNote, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expenseNote);
      });

      it('should return initial array if no ExpenseNote is added', () => {
        const expenseNoteCollection: IExpenseNote[] = [sampleWithRequiredData];
        expectedResult = service.addExpenseNoteToCollectionIfMissing(expenseNoteCollection, undefined, null);
        expect(expectedResult).toEqual(expenseNoteCollection);
      });
    });

    describe('compareExpenseNote', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExpenseNote(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExpenseNote(entity1, entity2);
        const compareResult2 = service.compareExpenseNote(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExpenseNote(entity1, entity2);
        const compareResult2 = service.compareExpenseNote(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExpenseNote(entity1, entity2);
        const compareResult2 = service.compareExpenseNote(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
