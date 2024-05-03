import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IMileageAllowance } from 'app/entities/mileage-allowance/mileage-allowance.model';
import { MileageAllowanceService } from 'app/entities/mileage-allowance/service/mileage-allowance.service';
import { IDocument } from 'app/entities/document/document.model';
import { DocumentService } from 'app/entities/document/service/document.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IExpenseNote } from '../expense-note.model';
import { ExpenseNoteService } from '../service/expense-note.service';
import { ExpenseNoteFormService } from './expense-note-form.service';

import { ExpenseNoteUpdateComponent } from './expense-note-update.component';

describe('ExpenseNote Management Update Component', () => {
  let comp: ExpenseNoteUpdateComponent;
  let fixture: ComponentFixture<ExpenseNoteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expenseNoteFormService: ExpenseNoteFormService;
  let expenseNoteService: ExpenseNoteService;
  let mileageAllowanceService: MileageAllowanceService;
  let documentService: DocumentService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ExpenseNoteUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ExpenseNoteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpenseNoteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expenseNoteFormService = TestBed.inject(ExpenseNoteFormService);
    expenseNoteService = TestBed.inject(ExpenseNoteService);
    mileageAllowanceService = TestBed.inject(MileageAllowanceService);
    documentService = TestBed.inject(DocumentService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call mileageAllowance query and add missing value', () => {
      const expenseNote: IExpenseNote = { id: 456 };
      const mileageAllowance: IMileageAllowance = { id: 25198 };
      expenseNote.mileageAllowance = mileageAllowance;

      const mileageAllowanceCollection: IMileageAllowance[] = [{ id: 23987 }];
      jest.spyOn(mileageAllowanceService, 'query').mockReturnValue(of(new HttpResponse({ body: mileageAllowanceCollection })));
      const expectedCollection: IMileageAllowance[] = [mileageAllowance, ...mileageAllowanceCollection];
      jest.spyOn(mileageAllowanceService, 'addMileageAllowanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expenseNote });
      comp.ngOnInit();

      expect(mileageAllowanceService.query).toHaveBeenCalled();
      expect(mileageAllowanceService.addMileageAllowanceToCollectionIfMissing).toHaveBeenCalledWith(
        mileageAllowanceCollection,
        mileageAllowance,
      );
      expect(comp.mileageAllowancesCollection).toEqual(expectedCollection);
    });

    it('Should call document query and add missing value', () => {
      const expenseNote: IExpenseNote = { id: 456 };
      const document: IDocument = { id: 8972 };
      expenseNote.document = document;

      const documentCollection: IDocument[] = [{ id: 21200 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const expectedCollection: IDocument[] = [document, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expenseNote });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(documentCollection, document);
      expect(comp.documentsCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const expenseNote: IExpenseNote = { id: 456 };
      const employee: IEmployee = { id: 4759 };
      expenseNote.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 2700 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expenseNote });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const expenseNote: IExpenseNote = { id: 456 };
      const mileageAllowance: IMileageAllowance = { id: 29273 };
      expenseNote.mileageAllowance = mileageAllowance;
      const document: IDocument = { id: 19178 };
      expenseNote.document = document;
      const employee: IEmployee = { id: 27818 };
      expenseNote.employee = employee;

      activatedRoute.data = of({ expenseNote });
      comp.ngOnInit();

      expect(comp.mileageAllowancesCollection).toContain(mileageAllowance);
      expect(comp.documentsCollection).toContain(document);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.expenseNote).toEqual(expenseNote);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseNote>>();
      const expenseNote = { id: 123 };
      jest.spyOn(expenseNoteFormService, 'getExpenseNote').mockReturnValue(expenseNote);
      jest.spyOn(expenseNoteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseNote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseNote }));
      saveSubject.complete();

      // THEN
      expect(expenseNoteFormService.getExpenseNote).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(expenseNoteService.update).toHaveBeenCalledWith(expect.objectContaining(expenseNote));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseNote>>();
      const expenseNote = { id: 123 };
      jest.spyOn(expenseNoteFormService, 'getExpenseNote').mockReturnValue({ id: null });
      jest.spyOn(expenseNoteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseNote: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expenseNote }));
      saveSubject.complete();

      // THEN
      expect(expenseNoteFormService.getExpenseNote).toHaveBeenCalled();
      expect(expenseNoteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpenseNote>>();
      const expenseNote = { id: 123 };
      jest.spyOn(expenseNoteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expenseNote });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expenseNoteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMileageAllowance', () => {
      it('Should forward to mileageAllowanceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(mileageAllowanceService, 'compareMileageAllowance');
        comp.compareMileageAllowance(entity, entity2);
        expect(mileageAllowanceService.compareMileageAllowance).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDocument', () => {
      it('Should forward to documentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(documentService, 'compareDocument');
        comp.compareDocument(entity, entity2);
        expect(documentService.compareDocument).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
