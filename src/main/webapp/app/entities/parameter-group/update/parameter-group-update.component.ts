import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IParameterGroup } from '../parameter-group.model';
import { ParameterGroupService } from '../service/parameter-group.service';
import { ParameterGroupFormService, ParameterGroupFormGroup } from './parameter-group-form.service';

@Component({
  standalone: true,
  selector: 'jhi-parameter-group-update',
  templateUrl: './parameter-group-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ParameterGroupUpdateComponent implements OnInit {
  isSaving = false;
  parameterGroup: IParameterGroup | null = null;

  protected parameterGroupService = inject(ParameterGroupService);
  protected parameterGroupFormService = inject(ParameterGroupFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ParameterGroupFormGroup = this.parameterGroupFormService.createParameterGroupFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parameterGroup }) => {
      this.parameterGroup = parameterGroup;
      if (parameterGroup) {
        this.updateForm(parameterGroup);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parameterGroup = this.parameterGroupFormService.getParameterGroup(this.editForm);
    if (parameterGroup.id !== null) {
      this.subscribeToSaveResponse(this.parameterGroupService.update(parameterGroup));
    } else {
      this.subscribeToSaveResponse(this.parameterGroupService.create(parameterGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParameterGroup>>): void {
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

  protected updateForm(parameterGroup: IParameterGroup): void {
    this.parameterGroup = parameterGroup;
    this.parameterGroupFormService.resetForm(this.editForm, parameterGroup);
  }
}
