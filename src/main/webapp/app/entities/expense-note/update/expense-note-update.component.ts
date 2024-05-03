import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMileageAllowance } from 'app/entities/mileage-allowance/mileage-allowance.model';
import { MileageAllowanceService } from 'app/entities/mileage-allowance/service/mileage-allowance.service';
import { IDocument } from 'app/entities/document/document.model';
import { DocumentService } from 'app/entities/document/service/document.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { ExpenseNoteService } from '../service/expense-note.service';
import { IExpenseNote } from '../expense-note.model';
import { ExpenseNoteFormService, ExpenseNoteFormGroup } from './expense-note-form.service';

@Component({
  standalone: true,
  selector: 'jhi-expense-note-update',
  templateUrl: './expense-note-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExpenseNoteUpdateComponent implements OnInit {
  isSaving = false;
  expenseNote: IExpenseNote | null = null;

  mileageAllowancesCollection: IMileageAllowance[] = [];
  documentsCollection: IDocument[] = [];
  employeesSharedCollection: IEmployee[] = [];

  protected expenseNoteService = inject(ExpenseNoteService);
  protected expenseNoteFormService = inject(ExpenseNoteFormService);
  protected mileageAllowanceService = inject(MileageAllowanceService);
  protected documentService = inject(DocumentService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExpenseNoteFormGroup = this.expenseNoteFormService.createExpenseNoteFormGroup();

  compareMileageAllowance = (o1: IMileageAllowance | null, o2: IMileageAllowance | null): boolean =>
    this.mileageAllowanceService.compareMileageAllowance(o1, o2);

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseNote }) => {
      this.expenseNote = expenseNote;
      if (expenseNote) {
        this.updateForm(expenseNote);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenseNote = this.expenseNoteFormService.getExpenseNote(this.editForm);
    if (expenseNote.id !== null) {
      this.subscribeToSaveResponse(this.expenseNoteService.update(expenseNote));
    } else {
      this.subscribeToSaveResponse(this.expenseNoteService.create(expenseNote));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseNote>>): void {
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

  protected updateForm(expenseNote: IExpenseNote): void {
    this.expenseNote = expenseNote;
    this.expenseNoteFormService.resetForm(this.editForm, expenseNote);

    this.mileageAllowancesCollection = this.mileageAllowanceService.addMileageAllowanceToCollectionIfMissing<IMileageAllowance>(
      this.mileageAllowancesCollection,
      expenseNote.mileageAllowance,
    );
    this.documentsCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(
      this.documentsCollection,
      expenseNote.document,
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      expenseNote.employee,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.mileageAllowanceService
      .query({ filter: 'expensenote-is-null' })
      .pipe(map((res: HttpResponse<IMileageAllowance[]>) => res.body ?? []))
      .pipe(
        map((mileageAllowances: IMileageAllowance[]) =>
          this.mileageAllowanceService.addMileageAllowanceToCollectionIfMissing<IMileageAllowance>(
            mileageAllowances,
            this.expenseNote?.mileageAllowance,
          ),
        ),
      )
      .subscribe((mileageAllowances: IMileageAllowance[]) => (this.mileageAllowancesCollection = mileageAllowances));

    this.documentService
      .query({ filter: 'expensenote-is-null' })
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.expenseNote?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsCollection = documents));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.expenseNote?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
