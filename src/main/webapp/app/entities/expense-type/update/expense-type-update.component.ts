import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IExpenseType } from '../expense-type.model';
import { ExpenseTypeService } from '../service/expense-type.service';
import { ExpenseTypeFormService, ExpenseTypeFormGroup } from './expense-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-expense-type-update',
  templateUrl: './expense-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExpenseTypeUpdateComponent implements OnInit {
  isSaving = false;
  expenseType: IExpenseType | null = null;

  protected expenseTypeService = inject(ExpenseTypeService);
  protected expenseTypeFormService = inject(ExpenseTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExpenseTypeFormGroup = this.expenseTypeFormService.createExpenseTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseType }) => {
      this.expenseType = expenseType;
      if (expenseType) {
        this.updateForm(expenseType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenseType = this.expenseTypeFormService.getExpenseType(this.editForm);
    if (expenseType.id !== null) {
      this.subscribeToSaveResponse(this.expenseTypeService.update(expenseType));
    } else {
      this.subscribeToSaveResponse(this.expenseTypeService.create(expenseType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseType>>): void {
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

  protected updateForm(expenseType: IExpenseType): void {
    this.expenseType = expenseType;
    this.expenseTypeFormService.resetForm(this.editForm, expenseType);
  }
}
