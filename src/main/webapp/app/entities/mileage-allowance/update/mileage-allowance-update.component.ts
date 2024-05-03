import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMileageAllowance } from '../mileage-allowance.model';
import { MileageAllowanceService } from '../service/mileage-allowance.service';
import { MileageAllowanceFormService, MileageAllowanceFormGroup } from './mileage-allowance-form.service';

@Component({
  standalone: true,
  selector: 'jhi-mileage-allowance-update',
  templateUrl: './mileage-allowance-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MileageAllowanceUpdateComponent implements OnInit {
  isSaving = false;
  mileageAllowance: IMileageAllowance | null = null;

  protected mileageAllowanceService = inject(MileageAllowanceService);
  protected mileageAllowanceFormService = inject(MileageAllowanceFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MileageAllowanceFormGroup = this.mileageAllowanceFormService.createMileageAllowanceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mileageAllowance }) => {
      this.mileageAllowance = mileageAllowance;
      if (mileageAllowance) {
        this.updateForm(mileageAllowance);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mileageAllowance = this.mileageAllowanceFormService.getMileageAllowance(this.editForm);
    if (mileageAllowance.id !== null) {
      this.subscribeToSaveResponse(this.mileageAllowanceService.update(mileageAllowance));
    } else {
      this.subscribeToSaveResponse(this.mileageAllowanceService.create(mileageAllowance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMileageAllowance>>): void {
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

  protected updateForm(mileageAllowance: IMileageAllowance): void {
    this.mileageAllowance = mileageAllowance;
    this.mileageAllowanceFormService.resetForm(this.editForm, mileageAllowance);
  }
}
