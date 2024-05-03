import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IServiceContract } from '../service-contract.model';
import { ServiceContractService } from '../service/service-contract.service';
import { ServiceContractFormService, ServiceContractFormGroup } from './service-contract-form.service';

@Component({
  standalone: true,
  selector: 'jhi-service-contract-update',
  templateUrl: './service-contract-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ServiceContractUpdateComponent implements OnInit {
  isSaving = false;
  serviceContract: IServiceContract | null = null;

  employeesCollection: IClient[] = [];
  serviceContractsSharedCollection: IServiceContract[] = [];

  protected serviceContractService = inject(ServiceContractService);
  protected serviceContractFormService = inject(ServiceContractFormService);
  protected clientService = inject(ClientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServiceContractFormGroup = this.serviceContractFormService.createServiceContractFormGroup();

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  compareServiceContract = (o1: IServiceContract | null, o2: IServiceContract | null): boolean =>
    this.serviceContractService.compareServiceContract(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceContract }) => {
      this.serviceContract = serviceContract;
      if (serviceContract) {
        this.updateForm(serviceContract);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceContract = this.serviceContractFormService.getServiceContract(this.editForm);
    if (serviceContract.id !== null) {
      this.subscribeToSaveResponse(this.serviceContractService.update(serviceContract));
    } else {
      this.subscribeToSaveResponse(this.serviceContractService.create(serviceContract));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceContract>>): void {
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

  protected updateForm(serviceContract: IServiceContract): void {
    this.serviceContract = serviceContract;
    this.serviceContractFormService.resetForm(this.editForm, serviceContract);

    this.employeesCollection = this.clientService.addClientToCollectionIfMissing<IClient>(
      this.employeesCollection,
      serviceContract.employee,
    );
    this.serviceContractsSharedCollection = this.serviceContractService.addServiceContractToCollectionIfMissing<IServiceContract>(
      this.serviceContractsSharedCollection,
      serviceContract.serviceContract,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query({ filter: 'servicecontract-is-null' })
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(
        map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.serviceContract?.employee)),
      )
      .subscribe((clients: IClient[]) => (this.employeesCollection = clients));

    this.serviceContractService
      .query()
      .pipe(map((res: HttpResponse<IServiceContract[]>) => res.body ?? []))
      .pipe(
        map((serviceContracts: IServiceContract[]) =>
          this.serviceContractService.addServiceContractToCollectionIfMissing<IServiceContract>(
            serviceContracts,
            this.serviceContract?.serviceContract,
          ),
        ),
      )
      .subscribe((serviceContracts: IServiceContract[]) => (this.serviceContractsSharedCollection = serviceContracts));
  }
}
