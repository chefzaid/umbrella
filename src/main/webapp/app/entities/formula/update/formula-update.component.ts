import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFormula } from '../formula.model';
import { FormulaService } from '../service/formula.service';
import { FormulaFormService, FormulaFormGroup } from './formula-form.service';

@Component({
  standalone: true,
  selector: 'jhi-formula-update',
  templateUrl: './formula-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FormulaUpdateComponent implements OnInit {
  isSaving = false;
  formula: IFormula | null = null;

  protected formulaService = inject(FormulaService);
  protected formulaFormService = inject(FormulaFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FormulaFormGroup = this.formulaFormService.createFormulaFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formula }) => {
      this.formula = formula;
      if (formula) {
        this.updateForm(formula);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formula = this.formulaFormService.getFormula(this.editForm);
    if (formula.id !== null) {
      this.subscribeToSaveResponse(this.formulaService.update(formula));
    } else {
      this.subscribeToSaveResponse(this.formulaService.create(formula));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormula>>): void {
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

  protected updateForm(formula: IFormula): void {
    this.formula = formula;
    this.formulaFormService.resetForm(this.editForm, formula);
  }
}
