import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EnterpriseService } from '../service/enterprise.service';
import { IEnterprise } from '../enterprise.model';
import { EnterpriseFormService, EnterpriseFormGroup } from './enterprise-form.service';

@Component({
  standalone: true,
  selector: 'jhi-enterprise-update',
  templateUrl: './enterprise-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EnterpriseUpdateComponent implements OnInit {
  isSaving = false;
  enterprise: IEnterprise | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected enterpriseService = inject(EnterpriseService);
  protected enterpriseFormService = inject(EnterpriseFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EnterpriseFormGroup = this.enterpriseFormService.createEnterpriseFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enterprise }) => {
      this.enterprise = enterprise;
      if (enterprise) {
        this.updateForm(enterprise);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('umbrellaApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enterprise = this.enterpriseFormService.getEnterprise(this.editForm);
    if (enterprise.id !== null) {
      this.subscribeToSaveResponse(this.enterpriseService.update(enterprise));
    } else {
      this.subscribeToSaveResponse(this.enterpriseService.create(enterprise));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnterprise>>): void {
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

  protected updateForm(enterprise: IEnterprise): void {
    this.enterprise = enterprise;
    this.enterpriseFormService.resetForm(this.editForm, enterprise);
  }
}
