import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IActivityReport } from 'app/entities/activity-report/activity-report.model';
import { ActivityReportService } from 'app/entities/activity-report/service/activity-report.service';
import { OperationType } from 'app/entities/enumerations/operation-type.model';
import { OperationService } from '../service/operation.service';
import { IOperation } from '../operation.model';
import { OperationFormService, OperationFormGroup } from './operation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-operation-update',
  templateUrl: './operation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OperationUpdateComponent implements OnInit {
  isSaving = false;
  operation: IOperation | null = null;
  operationTypeValues = Object.keys(OperationType);

  activityReportsSharedCollection: IActivityReport[] = [];

  protected operationService = inject(OperationService);
  protected operationFormService = inject(OperationFormService);
  protected activityReportService = inject(ActivityReportService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OperationFormGroup = this.operationFormService.createOperationFormGroup();

  compareActivityReport = (o1: IActivityReport | null, o2: IActivityReport | null): boolean =>
    this.activityReportService.compareActivityReport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operation }) => {
      this.operation = operation;
      if (operation) {
        this.updateForm(operation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operation = this.operationFormService.getOperation(this.editForm);
    if (operation.id !== null) {
      this.subscribeToSaveResponse(this.operationService.update(operation));
    } else {
      this.subscribeToSaveResponse(this.operationService.create(operation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperation>>): void {
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

  protected updateForm(operation: IOperation): void {
    this.operation = operation;
    this.operationFormService.resetForm(this.editForm, operation);

    this.activityReportsSharedCollection = this.activityReportService.addActivityReportToCollectionIfMissing<IActivityReport>(
      this.activityReportsSharedCollection,
      operation.activityReport,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.activityReportService
      .query()
      .pipe(map((res: HttpResponse<IActivityReport[]>) => res.body ?? []))
      .pipe(
        map((activityReports: IActivityReport[]) =>
          this.activityReportService.addActivityReportToCollectionIfMissing<IActivityReport>(
            activityReports,
            this.operation?.activityReport,
          ),
        ),
      )
      .subscribe((activityReports: IActivityReport[]) => (this.activityReportsSharedCollection = activityReports));
  }
}
