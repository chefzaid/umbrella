import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IEnterprise } from 'app/entities/enterprise/enterprise.model';
import { EnterpriseService } from 'app/entities/enterprise/service/enterprise.service';
import { ParameterService } from '../service/parameter.service';
import { IParameter } from '../parameter.model';
import { ParameterFormService, ParameterFormGroup } from './parameter-form.service';

@Component({
  standalone: true,
  selector: 'jhi-parameter-update',
  templateUrl: './parameter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ParameterUpdateComponent implements OnInit {
  isSaving = false;
  parameter: IParameter | null = null;

  appUsersSharedCollection: IAppUser[] = [];
  enterprisesSharedCollection: IEnterprise[] = [];

  protected parameterService = inject(ParameterService);
  protected parameterFormService = inject(ParameterFormService);
  protected appUserService = inject(AppUserService);
  protected enterpriseService = inject(EnterpriseService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ParameterFormGroup = this.parameterFormService.createParameterFormGroup();

  compareAppUser = (o1: IAppUser | null, o2: IAppUser | null): boolean => this.appUserService.compareAppUser(o1, o2);

  compareEnterprise = (o1: IEnterprise | null, o2: IEnterprise | null): boolean => this.enterpriseService.compareEnterprise(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parameter }) => {
      this.parameter = parameter;
      if (parameter) {
        this.updateForm(parameter);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parameter = this.parameterFormService.getParameter(this.editForm);
    if (parameter.id !== null) {
      this.subscribeToSaveResponse(this.parameterService.update(parameter));
    } else {
      this.subscribeToSaveResponse(this.parameterService.create(parameter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParameter>>): void {
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

  protected updateForm(parameter: IParameter): void {
    this.parameter = parameter;
    this.parameterFormService.resetForm(this.editForm, parameter);

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(
      this.appUsersSharedCollection,
      parameter.appUser,
    );
    this.enterprisesSharedCollection = this.enterpriseService.addEnterpriseToCollectionIfMissing<IEnterprise>(
      this.enterprisesSharedCollection,
      parameter.enterprise,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(appUsers, this.parameter?.appUser)))
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));

    this.enterpriseService
      .query()
      .pipe(map((res: HttpResponse<IEnterprise[]>) => res.body ?? []))
      .pipe(
        map((enterprises: IEnterprise[]) =>
          this.enterpriseService.addEnterpriseToCollectionIfMissing<IEnterprise>(enterprises, this.parameter?.enterprise),
        ),
      )
      .subscribe((enterprises: IEnterprise[]) => (this.enterprisesSharedCollection = enterprises));
  }
}
