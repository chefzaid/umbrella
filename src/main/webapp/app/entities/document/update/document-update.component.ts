import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IParameter } from 'app/entities/parameter/parameter.model';
import { ParameterService } from 'app/entities/parameter/service/parameter.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IEmploymentContract } from 'app/entities/employment-contract/employment-contract.model';
import { EmploymentContractService } from 'app/entities/employment-contract/service/employment-contract.service';
import { IServiceContract } from 'app/entities/service-contract/service-contract.model';
import { ServiceContractService } from 'app/entities/service-contract/service/service-contract.service';
import { IEnterprise } from 'app/entities/enterprise/enterprise.model';
import { EnterpriseService } from 'app/entities/enterprise/service/enterprise.service';
import { DocumentService } from '../service/document.service';
import { IDocument } from '../document.model';
import { DocumentFormService, DocumentFormGroup } from './document-form.service';

@Component({
  standalone: true,
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentUpdateComponent implements OnInit {
  isSaving = false;
  document: IDocument | null = null;

  documentTypesCollection: IParameter[] = [];
  employeesSharedCollection: IEmployee[] = [];
  employmentContractsSharedCollection: IEmploymentContract[] = [];
  serviceContractsSharedCollection: IServiceContract[] = [];
  enterprisesSharedCollection: IEnterprise[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected documentService = inject(DocumentService);
  protected documentFormService = inject(DocumentFormService);
  protected parameterService = inject(ParameterService);
  protected employeeService = inject(EmployeeService);
  protected employmentContractService = inject(EmploymentContractService);
  protected serviceContractService = inject(ServiceContractService);
  protected enterpriseService = inject(EnterpriseService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentFormGroup = this.documentFormService.createDocumentFormGroup();

  compareParameter = (o1: IParameter | null, o2: IParameter | null): boolean => this.parameterService.compareParameter(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareEmploymentContract = (o1: IEmploymentContract | null, o2: IEmploymentContract | null): boolean =>
    this.employmentContractService.compareEmploymentContract(o1, o2);

  compareServiceContract = (o1: IServiceContract | null, o2: IServiceContract | null): boolean =>
    this.serviceContractService.compareServiceContract(o1, o2);

  compareEnterprise = (o1: IEnterprise | null, o2: IEnterprise | null): boolean => this.enterpriseService.compareEnterprise(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ document }) => {
      this.document = document;
      if (document) {
        this.updateForm(document);
      }

      this.loadRelationshipsOptions();
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
    const document = this.documentFormService.getDocument(this.editForm);
    if (document.id !== null) {
      this.subscribeToSaveResponse(this.documentService.update(document));
    } else {
      this.subscribeToSaveResponse(this.documentService.create(document));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocument>>): void {
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

  protected updateForm(document: IDocument): void {
    this.document = document;
    this.documentFormService.resetForm(this.editForm, document);

    this.documentTypesCollection = this.parameterService.addParameterToCollectionIfMissing<IParameter>(
      this.documentTypesCollection,
      document.documentType,
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      document.employee,
    );
    this.employmentContractsSharedCollection =
      this.employmentContractService.addEmploymentContractToCollectionIfMissing<IEmploymentContract>(
        this.employmentContractsSharedCollection,
        document.employmentContract,
      );
    this.serviceContractsSharedCollection = this.serviceContractService.addServiceContractToCollectionIfMissing<IServiceContract>(
      this.serviceContractsSharedCollection,
      document.serviceContract,
    );
    this.enterprisesSharedCollection = this.enterpriseService.addEnterpriseToCollectionIfMissing<IEnterprise>(
      this.enterprisesSharedCollection,
      document.enterprise,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.parameterService
      .query({ filter: 'document-is-null' })
      .pipe(map((res: HttpResponse<IParameter[]>) => res.body ?? []))
      .pipe(
        map((parameters: IParameter[]) =>
          this.parameterService.addParameterToCollectionIfMissing<IParameter>(parameters, this.document?.documentType),
        ),
      )
      .subscribe((parameters: IParameter[]) => (this.documentTypesCollection = parameters));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.document?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.employmentContractService
      .query()
      .pipe(map((res: HttpResponse<IEmploymentContract[]>) => res.body ?? []))
      .pipe(
        map((employmentContracts: IEmploymentContract[]) =>
          this.employmentContractService.addEmploymentContractToCollectionIfMissing<IEmploymentContract>(
            employmentContracts,
            this.document?.employmentContract,
          ),
        ),
      )
      .subscribe((employmentContracts: IEmploymentContract[]) => (this.employmentContractsSharedCollection = employmentContracts));

    this.serviceContractService
      .query()
      .pipe(map((res: HttpResponse<IServiceContract[]>) => res.body ?? []))
      .pipe(
        map((serviceContracts: IServiceContract[]) =>
          this.serviceContractService.addServiceContractToCollectionIfMissing<IServiceContract>(
            serviceContracts,
            this.document?.serviceContract,
          ),
        ),
      )
      .subscribe((serviceContracts: IServiceContract[]) => (this.serviceContractsSharedCollection = serviceContracts));

    this.enterpriseService
      .query()
      .pipe(map((res: HttpResponse<IEnterprise[]>) => res.body ?? []))
      .pipe(
        map((enterprises: IEnterprise[]) =>
          this.enterpriseService.addEnterpriseToCollectionIfMissing<IEnterprise>(enterprises, this.document?.enterprise),
        ),
      )
      .subscribe((enterprises: IEnterprise[]) => (this.enterprisesSharedCollection = enterprises));
  }
}
