import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITimeSheet } from 'app/entities/time-sheet/time-sheet.model';
import { TimeSheetService } from 'app/entities/time-sheet/service/time-sheet.service';
import { ITimeSheetLine } from '../time-sheet-line.model';
import { TimeSheetLineService } from '../service/time-sheet-line.service';
import { TimeSheetLineFormService, TimeSheetLineFormGroup } from './time-sheet-line-form.service';

@Component({
  standalone: true,
  selector: 'jhi-time-sheet-line-update',
  templateUrl: './time-sheet-line-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TimeSheetLineUpdateComponent implements OnInit {
  isSaving = false;
  timeSheetLine: ITimeSheetLine | null = null;

  timeSheetsSharedCollection: ITimeSheet[] = [];

  protected timeSheetLineService = inject(TimeSheetLineService);
  protected timeSheetLineFormService = inject(TimeSheetLineFormService);
  protected timeSheetService = inject(TimeSheetService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TimeSheetLineFormGroup = this.timeSheetLineFormService.createTimeSheetLineFormGroup();

  compareTimeSheet = (o1: ITimeSheet | null, o2: ITimeSheet | null): boolean => this.timeSheetService.compareTimeSheet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ timeSheetLine }) => {
      this.timeSheetLine = timeSheetLine;
      if (timeSheetLine) {
        this.updateForm(timeSheetLine);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const timeSheetLine = this.timeSheetLineFormService.getTimeSheetLine(this.editForm);
    if (timeSheetLine.id !== null) {
      this.subscribeToSaveResponse(this.timeSheetLineService.update(timeSheetLine));
    } else {
      this.subscribeToSaveResponse(this.timeSheetLineService.create(timeSheetLine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITimeSheetLine>>): void {
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

  protected updateForm(timeSheetLine: ITimeSheetLine): void {
    this.timeSheetLine = timeSheetLine;
    this.timeSheetLineFormService.resetForm(this.editForm, timeSheetLine);

    this.timeSheetsSharedCollection = this.timeSheetService.addTimeSheetToCollectionIfMissing<ITimeSheet>(
      this.timeSheetsSharedCollection,
      timeSheetLine.timeSheet,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.timeSheetService
      .query()
      .pipe(map((res: HttpResponse<ITimeSheet[]>) => res.body ?? []))
      .pipe(
        map((timeSheets: ITimeSheet[]) =>
          this.timeSheetService.addTimeSheetToCollectionIfMissing<ITimeSheet>(timeSheets, this.timeSheetLine?.timeSheet),
        ),
      )
      .subscribe((timeSheets: ITimeSheet[]) => (this.timeSheetsSharedCollection = timeSheets));
  }
}
