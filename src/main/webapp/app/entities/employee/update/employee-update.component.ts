import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { IEmploymentContract } from 'app/entities/employment-contract/employment-contract.model';
import { EmploymentContractService } from 'app/entities/employment-contract/service/employment-contract.service';
import { IIdDocument } from 'app/entities/id-document/id-document.model';
import { IdDocumentService } from 'app/entities/id-document/service/id-document.service';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { WalletService } from 'app/entities/wallet/service/wallet.service';
import { Gender } from 'app/entities/enumerations/gender.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';
import { EmployeeService } from '../service/employee.service';
import { IEmployee } from '../employee.model';
import { EmployeeFormService, EmployeeFormGroup } from './employee-form.service';

@Component({
  standalone: true,
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  employee: IEmployee | null = null;
  genderValues = Object.keys(Gender);
  maritalStatusValues = Object.keys(MaritalStatus);

  usersCollection: IAppUser[] = [];
  addressesCollection: IAddress[] = [];
  contractsCollection: IEmploymentContract[] = [];
  idDocumentsCollection: IIdDocument[] = [];
  walletsCollection: IWallet[] = [];
  employeesSharedCollection: IEmployee[] = [];

  protected employeeService = inject(EmployeeService);
  protected employeeFormService = inject(EmployeeFormService);
  protected appUserService = inject(AppUserService);
  protected addressService = inject(AddressService);
  protected employmentContractService = inject(EmploymentContractService);
  protected idDocumentService = inject(IdDocumentService);
  protected walletService = inject(WalletService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmployeeFormGroup = this.employeeFormService.createEmployeeFormGroup();

  compareAppUser = (o1: IAppUser | null, o2: IAppUser | null): boolean => this.appUserService.compareAppUser(o1, o2);

  compareAddress = (o1: IAddress | null, o2: IAddress | null): boolean => this.addressService.compareAddress(o1, o2);

  compareEmploymentContract = (o1: IEmploymentContract | null, o2: IEmploymentContract | null): boolean =>
    this.employmentContractService.compareEmploymentContract(o1, o2);

  compareIdDocument = (o1: IIdDocument | null, o2: IIdDocument | null): boolean => this.idDocumentService.compareIdDocument(o1, o2);

  compareWallet = (o1: IWallet | null, o2: IWallet | null): boolean => this.walletService.compareWallet(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.employee = employee;
      if (employee) {
        this.updateForm(employee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.employeeFormService.getEmployee(this.editForm);
    if (employee.id !== null) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.employee = employee;
    this.employeeFormService.resetForm(this.editForm, employee);

    this.usersCollection = this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(this.usersCollection, employee.user);
    this.addressesCollection = this.addressService.addAddressToCollectionIfMissing<IAddress>(this.addressesCollection, employee.address);
    this.contractsCollection = this.employmentContractService.addEmploymentContractToCollectionIfMissing<IEmploymentContract>(
      this.contractsCollection,
      employee.contract,
    );
    this.idDocumentsCollection = this.idDocumentService.addIdDocumentToCollectionIfMissing<IIdDocument>(
      this.idDocumentsCollection,
      employee.idDocument,
    );
    this.walletsCollection = this.walletService.addWalletToCollectionIfMissing<IWallet>(this.walletsCollection, employee.wallet);
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      employee.manager,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(appUsers, this.employee?.user)))
      .subscribe((appUsers: IAppUser[]) => (this.usersCollection = appUsers));

    this.addressService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<IAddress[]>) => res.body ?? []))
      .pipe(
        map((addresses: IAddress[]) => this.addressService.addAddressToCollectionIfMissing<IAddress>(addresses, this.employee?.address)),
      )
      .subscribe((addresses: IAddress[]) => (this.addressesCollection = addresses));

    this.employmentContractService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<IEmploymentContract[]>) => res.body ?? []))
      .pipe(
        map((employmentContracts: IEmploymentContract[]) =>
          this.employmentContractService.addEmploymentContractToCollectionIfMissing<IEmploymentContract>(
            employmentContracts,
            this.employee?.contract,
          ),
        ),
      )
      .subscribe((employmentContracts: IEmploymentContract[]) => (this.contractsCollection = employmentContracts));

    this.idDocumentService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<IIdDocument[]>) => res.body ?? []))
      .pipe(
        map((idDocuments: IIdDocument[]) =>
          this.idDocumentService.addIdDocumentToCollectionIfMissing<IIdDocument>(idDocuments, this.employee?.idDocument),
        ),
      )
      .subscribe((idDocuments: IIdDocument[]) => (this.idDocumentsCollection = idDocuments));

    this.walletService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<IWallet[]>) => res.body ?? []))
      .pipe(map((wallets: IWallet[]) => this.walletService.addWalletToCollectionIfMissing<IWallet>(wallets, this.employee?.wallet)))
      .subscribe((wallets: IWallet[]) => (this.walletsCollection = wallets));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.employee?.manager),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
