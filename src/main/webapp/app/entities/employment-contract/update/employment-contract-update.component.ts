import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFormula } from 'app/entities/formula/formula.model';
import { FormulaService } from 'app/entities/formula/service/formula.service';
import { EmploymentContractType } from 'app/entities/enumerations/employment-contract-type.model';
import { EmploymentContractService } from '../service/employment-contract.service';
import { IEmploymentContract } from '../employment-contract.model';
import { EmploymentContractFormService, EmploymentContractFormGroup } from './employment-contract-form.service';

@Component({
  standalone: true,
  selector: 'jhi-employment-contract-update',
  templateUrl: './employment-contract-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmploymentContractUpdateComponent implements OnInit {
  isSaving = false;
  employmentContract: IEmploymentContract | null = null;
  employmentContractTypeValues = Object.keys(EmploymentContractType);

  forumulasCollection: IFormula[] = [];
  employmentContractsSharedCollection: IEmploymentContract[] = [];

  protected employmentContractService = inject(EmploymentContractService);
  protected employmentContractFormService = inject(EmploymentContractFormService);
  protected formulaService = inject(FormulaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmploymentContractFormGroup = this.employmentContractFormService.createEmploymentContractFormGroup();

  compareFormula = (o1: IFormula | null, o2: IFormula | null): boolean => this.formulaService.compareFormula(o1, o2);

  compareEmploymentContract = (o1: IEmploymentContract | null, o2: IEmploymentContract | null): boolean =>
    this.employmentContractService.compareEmploymentContract(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentContract }) => {
      this.employmentContract = employmentContract;
      if (employmentContract) {
        this.updateForm(employmentContract);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employmentContract = this.employmentContractFormService.getEmploymentContract(this.editForm);
    if (employmentContract.id !== null) {
      this.subscribeToSaveResponse(this.employmentContractService.update(employmentContract));
    } else {
      this.subscribeToSaveResponse(this.employmentContractService.create(employmentContract));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmploymentContract>>): void {
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

  protected updateForm(employmentContract: IEmploymentContract): void {
    this.employmentContract = employmentContract;
    this.employmentContractFormService.resetForm(this.editForm, employmentContract);

    this.forumulasCollection = this.formulaService.addFormulaToCollectionIfMissing<IFormula>(
      this.forumulasCollection,
      employmentContract.forumula,
    );
    this.employmentContractsSharedCollection =
      this.employmentContractService.addEmploymentContractToCollectionIfMissing<IEmploymentContract>(
        this.employmentContractsSharedCollection,
        employmentContract.employmentContract,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.formulaService
      .query({ filter: 'employmentcontract-is-null' })
      .pipe(map((res: HttpResponse<IFormula[]>) => res.body ?? []))
      .pipe(
        map((formulas: IFormula[]) =>
          this.formulaService.addFormulaToCollectionIfMissing<IFormula>(formulas, this.employmentContract?.forumula),
        ),
      )
      .subscribe((formulas: IFormula[]) => (this.forumulasCollection = formulas));

    this.employmentContractService
      .query()
      .pipe(map((res: HttpResponse<IEmploymentContract[]>) => res.body ?? []))
      .pipe(
        map((employmentContracts: IEmploymentContract[]) =>
          this.employmentContractService.addEmploymentContractToCollectionIfMissing<IEmploymentContract>(
            employmentContracts,
            this.employmentContract?.employmentContract,
          ),
        ),
      )
      .subscribe((employmentContracts: IEmploymentContract[]) => (this.employmentContractsSharedCollection = employmentContracts));
  }
}
