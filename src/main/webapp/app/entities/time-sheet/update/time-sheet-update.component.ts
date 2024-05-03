import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IDocument } from 'app/entities/document/document.model';
import { DocumentService } from 'app/entities/document/service/document.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { TimeSheetService } from '../service/time-sheet.service';
import { ITimeSheet } from '../time-sheet.model';
import { TimeSheetFormService, TimeSheetFormGroup } from './time-sheet-form.service';

@Component({
  standalone: true,
  selector: 'jhi-time-sheet-update',
  templateUrl: './time-sheet-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TimeSheetUpdateComponent implements OnInit {
  isSaving = false;
  timeSheet: ITimeSheet | null = null;

  projectsCollection: IProject[] = [];
  documentsCollection: IDocument[] = [];
  employeesSharedCollection: IEmployee[] = [];

  protected timeSheetService = inject(TimeSheetService);
  protected timeSheetFormService = inject(TimeSheetFormService);
  protected projectService = inject(ProjectService);
  protected documentService = inject(DocumentService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TimeSheetFormGroup = this.timeSheetFormService.createTimeSheetFormGroup();

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ timeSheet }) => {
      this.timeSheet = timeSheet;
      if (timeSheet) {
        this.updateForm(timeSheet);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const timeSheet = this.timeSheetFormService.getTimeSheet(this.editForm);
    if (timeSheet.id !== null) {
      this.subscribeToSaveResponse(this.timeSheetService.update(timeSheet));
    } else {
      this.subscribeToSaveResponse(this.timeSheetService.create(timeSheet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITimeSheet>>): void {
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

  protected updateForm(timeSheet: ITimeSheet): void {
    this.timeSheet = timeSheet;
    this.timeSheetFormService.resetForm(this.editForm, timeSheet);

    this.projectsCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(this.projectsCollection, timeSheet.project);
    this.documentsCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(
      this.documentsCollection,
      timeSheet.document,
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      timeSheet.employee,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.projectService
      .query({ filter: 'timesheet-is-null' })
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing<IProject>(projects, this.timeSheet?.project)))
      .subscribe((projects: IProject[]) => (this.projectsCollection = projects));

    this.documentService
      .query({ filter: 'timesheet-is-null' })
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.timeSheet?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsCollection = documents));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.timeSheet?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
