import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IParameter } from 'app/entities/parameter/parameter.model';
import { ParameterService } from 'app/entities/parameter/service/parameter.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IExpenseNote } from 'app/entities/expense-note/expense-note.model';
import { ExpenseNoteService } from 'app/entities/expense-note/service/expense-note.service';
import { IExpense } from '../expense.model';
import { ExpenseService } from '../service/expense.service';
import { ExpenseFormService } from './expense-form.service';

import { ExpenseUpdateComponent } from './expense-update.component';

describe('Expense Management Update Component', () => {
  let comp: ExpenseUpdateComponent;
  let fixture: ComponentFixture<ExpenseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expenseFormService: ExpenseFormService;
  let expenseService: ExpenseService;
  let parameterService: ParameterService;
  let projectService: ProjectService;
  let expenseNoteService: ExpenseNoteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ExpenseUpdateComponent],
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
      .overrideTemplate(ExpenseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpenseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expenseFormService = TestBed.inject(ExpenseFormService);
    expenseService = TestBed.inject(ExpenseService);
    parameterService = TestBed.inject(ParameterService);
    projectService = TestBed.inject(ProjectService);
    expenseNoteService = TestBed.inject(ExpenseNoteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call paymentMethod query and add missing value', () => {
      const expense: IExpense = { id: 456 };
      const paymentMethod: IParameter = { id: 6855 };
      expense.paymentMethod = paymentMethod;

      const paymentMethodCollection: IParameter[] = [{ id: 19972 }];
      jest.spyOn(parameterService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentMethodCollection })));
      const expectedCollection: IParameter[] = [paymentMethod, ...paymentMethodCollection];
      jest.spyOn(parameterService, 'addParameterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(parameterService.query).toHaveBeenCalled();
      expect(parameterService.addParameterToCollectionIfMissing).toHaveBeenCalledWith(paymentMethodCollection, paymentMethod);
      expect(comp.paymentMethodsCollection).toEqual(expectedCollection);
    });

    it('Should call project query and add missing value', () => {
      const expense: IExpense = { id: 456 };
      const project: IProject = { id: 5568 };
      expense.project = project;

      const projectCollection: IProject[] = [{ id: 6634 }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
      const expectedCollection: IProject[] = [project, ...projectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(projectCollection, project);
      expect(comp.projectsCollection).toEqual(expectedCollection);
    });

    it('Should call ExpenseNote query and add missing value', () => {
      const expense: IExpense = { id: 456 };
      const expenseNote: IExpenseNote = { id: 6583 };
      expense.expenseNote = expenseNote;

      const expenseNoteCollection: IExpenseNote[] = [{ id: 17915 }];
      jest.spyOn(expenseNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: expenseNoteCollection })));
      const additionalExpenseNotes = [expenseNote];
      const expectedCollection: IExpenseNote[] = [...additionalExpenseNotes, ...expenseNoteCollection];
      jest.spyOn(expenseNoteService, 'addExpenseNoteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(expenseNoteService.query).toHaveBeenCalled();
      expect(expenseNoteService.addExpenseNoteToCollectionIfMissing).toHaveBeenCalledWith(
        expenseNoteCollection,
        ...additionalExpenseNotes.map(expect.objectContaining),
      );
      expect(comp.expenseNotesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const expense: IExpense = { id: 456 };
      const paymentMethod: IParameter = { id: 20970 };
      expense.paymentMethod = paymentMethod;
      const project: IProject = { id: 24493 };
      expense.project = project;
      const expenseNote: IExpenseNote = { id: 12375 };
      expense.expenseNote = expenseNote;

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(comp.paymentMethodsCollection).toContain(paymentMethod);
      expect(comp.projectsCollection).toContain(project);
      expect(comp.expenseNotesSharedCollection).toContain(expenseNote);
      expect(comp.expense).toEqual(expense);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpense>>();
      const expense = { id: 123 };
      jest.spyOn(expenseFormService, 'getExpense').mockReturnValue(expense);
      jest.spyOn(expenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expense }));
      saveSubject.complete();

      // THEN
      expect(expenseFormService.getExpense).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(expenseService.update).toHaveBeenCalledWith(expect.objectContaining(expense));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpense>>();
      const expense = { id: 123 };
      jest.spyOn(expenseFormService, 'getExpense').mockReturnValue({ id: null });
      jest.spyOn(expenseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expense: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expense }));
      saveSubject.complete();

      // THEN
      expect(expenseFormService.getExpense).toHaveBeenCalled();
      expect(expenseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpense>>();
      const expense = { id: 123 };
      jest.spyOn(expenseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expenseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareParameter', () => {
      it('Should forward to parameterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(parameterService, 'compareParameter');
        comp.compareParameter(entity, entity2);
        expect(parameterService.compareParameter).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProject', () => {
      it('Should forward to projectService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(projectService, 'compareProject');
        comp.compareProject(entity, entity2);
        expect(projectService.compareProject).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareExpenseNote', () => {
      it('Should forward to expenseNoteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(expenseNoteService, 'compareExpenseNote');
        comp.compareExpenseNote(entity, entity2);
        expect(expenseNoteService.compareExpenseNote).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
