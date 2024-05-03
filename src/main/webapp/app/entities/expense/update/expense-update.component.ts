import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IParameter } from 'app/entities/parameter/parameter.model';
import { ParameterService } from 'app/entities/parameter/service/parameter.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IExpenseNote } from 'app/entities/expense-note/expense-note.model';
import { ExpenseNoteService } from 'app/entities/expense-note/service/expense-note.service';
import { ExpenseService } from '../service/expense.service';
import { IExpense } from '../expense.model';
import { ExpenseFormService, ExpenseFormGroup } from './expense-form.service';

@Component({
  standalone: true,
  selector: 'jhi-expense-update',
  templateUrl: './expense-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExpenseUpdateComponent implements OnInit {
  isSaving = false;
  expense: IExpense | null = null;

  paymentMethodsCollection: IParameter[] = [];
  projectsCollection: IProject[] = [];
  expenseNotesSharedCollection: IExpenseNote[] = [];

  protected expenseService = inject(ExpenseService);
  protected expenseFormService = inject(ExpenseFormService);
  protected parameterService = inject(ParameterService);
  protected projectService = inject(ProjectService);
  protected expenseNoteService = inject(ExpenseNoteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExpenseFormGroup = this.expenseFormService.createExpenseFormGroup();

  compareParameter = (o1: IParameter | null, o2: IParameter | null): boolean => this.parameterService.compareParameter(o1, o2);

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  compareExpenseNote = (o1: IExpenseNote | null, o2: IExpenseNote | null): boolean => this.expenseNoteService.compareExpenseNote(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expense }) => {
      this.expense = expense;
      if (expense) {
        this.updateForm(expense);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expense = this.expenseFormService.getExpense(this.editForm);
    if (expense.id !== null) {
      this.subscribeToSaveResponse(this.expenseService.update(expense));
    } else {
      this.subscribeToSaveResponse(this.expenseService.create(expense));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpense>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(expense: IExpense): void {
    this.expense = expense;
    this.expenseFormService.resetForm(this.editForm, expense);

    this.paymentMethodsCollection = this.parameterService.addParameterToCollectionIfMissing<IParameter>(
      this.paymentMethodsCollection,
      expense.paymentMethod,
    );
    this.projectsCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(this.projectsCollection, expense.project);
    this.expenseNotesSharedCollection = this.expenseNoteService.addExpenseNoteToCollectionIfMissing<IExpenseNote>(
      this.expenseNotesSharedCollection,
      expense.expenseNote,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.parameterService
      .query({ filter: 'expense-is-null' })
      .pipe(map((res: HttpResponse<IParameter[]>) => res.body ?? []))
      .pipe(
        map((parameters: IParameter[]) =>
          this.parameterService.addParameterToCollectionIfMissing<IParameter>(parameters, this.expense?.paymentMethod),
        ),
      )
      .subscribe((parameters: IParameter[]) => (this.paymentMethodsCollection = parameters));

    this.projectService
      .query({ filter: 'expense-is-null' })
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing<IProject>(projects, this.expense?.project)))
      .subscribe((projects: IProject[]) => (this.projectsCollection = projects));

    this.expenseNoteService
      .query()
      .pipe(map((res: HttpResponse<IExpenseNote[]>) => res.body ?? []))
      .pipe(
        map((expenseNotes: IExpenseNote[]) =>
          this.expenseNoteService.addExpenseNoteToCollectionIfMissing<IExpenseNote>(expenseNotes, this.expense?.expenseNote),
        ),
      )
      .subscribe((expenseNotes: IExpenseNote[]) => (this.expenseNotesSharedCollection = expenseNotes));
  }
}
