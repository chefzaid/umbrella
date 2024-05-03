import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IActivityReport } from '../activity-report.model';
import { ActivityReportService } from '../service/activity-report.service';
import { ActivityReportFormService, ActivityReportFormGroup } from './activity-report-form.service';

@Component({
  standalone: true,
  selector: 'jhi-activity-report-update',
  templateUrl: './activity-report-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ActivityReportUpdateComponent implements OnInit {
  isSaving = false;
  activityReport: IActivityReport | null = null;

  employeesSharedCollection: IEmployee[] = [];

  protected activityReportService = inject(ActivityReportService);
  protected activityReportFormService = inject(ActivityReportFormService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ActivityReportFormGroup = this.activityReportFormService.createActivityReportFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activityReport }) => {
      this.activityReport = activityReport;
      if (activityReport) {
        this.updateForm(activityReport);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const activityReport = this.activityReportFormService.getActivityReport(this.editForm);
    if (activityReport.id !== null) {
      this.subscribeToSaveResponse(this.activityReportService.update(activityReport));
    } else {
      this.subscribeToSaveResponse(this.activityReportService.create(activityReport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivityReport>>): void {
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

  protected updateForm(activityReport: IActivityReport): void {
    this.activityReport = activityReport;
    this.activityReportFormService.resetForm(this.editForm, activityReport);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      activityReport.employee,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.activityReport?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
