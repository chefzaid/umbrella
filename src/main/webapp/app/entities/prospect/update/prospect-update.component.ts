import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFormula } from 'app/entities/formula/formula.model';
import { FormulaService } from 'app/entities/formula/service/formula.service';
import { IProspect } from '../prospect.model';
import { ProspectService } from '../service/prospect.service';
import { ProspectFormService, ProspectFormGroup } from './prospect-form.service';

@Component({
  standalone: true,
  selector: 'jhi-prospect-update',
  templateUrl: './prospect-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProspectUpdateComponent implements OnInit {
  isSaving = false;
  prospect: IProspect | null = null;

  formulasCollection: IFormula[] = [];

  protected prospectService = inject(ProspectService);
  protected prospectFormService = inject(ProspectFormService);
  protected formulaService = inject(FormulaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProspectFormGroup = this.prospectFormService.createProspectFormGroup();

  compareFormula = (o1: IFormula | null, o2: IFormula | null): boolean => this.formulaService.compareFormula(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prospect }) => {
      this.prospect = prospect;
      if (prospect) {
        this.updateForm(prospect);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prospect = this.prospectFormService.getProspect(this.editForm);
    if (prospect.id !== null) {
      this.subscribeToSaveResponse(this.prospectService.update(prospect));
    } else {
      this.subscribeToSaveResponse(this.prospectService.create(prospect));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProspect>>): void {
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

  protected updateForm(prospect: IProspect): void {
    this.prospect = prospect;
    this.prospectFormService.resetForm(this.editForm, prospect);

    this.formulasCollection = this.formulaService.addFormulaToCollectionIfMissing<IFormula>(this.formulasCollection, prospect.formula);
  }

  protected loadRelationshipsOptions(): void {
    this.formulaService
      .query({ filter: 'prospect-is-null' })
      .pipe(map((res: HttpResponse<IFormula[]>) => res.body ?? []))
      .pipe(map((formulas: IFormula[]) => this.formulaService.addFormulaToCollectionIfMissing<IFormula>(formulas, this.prospect?.formula)))
      .subscribe((formulas: IFormula[]) => (this.formulasCollection = formulas));
  }
}
