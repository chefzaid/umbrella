import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDocument } from 'app/entities/document/document.model';
import { DocumentService } from 'app/entities/document/service/document.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { PaySlipService } from '../service/pay-slip.service';
import { IPaySlip } from '../pay-slip.model';
import { PaySlipFormService, PaySlipFormGroup } from './pay-slip-form.service';

@Component({
  standalone: true,
  selector: 'jhi-pay-slip-update',
  templateUrl: './pay-slip-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaySlipUpdateComponent implements OnInit {
  isSaving = false;
  paySlip: IPaySlip | null = null;

  documentsCollection: IDocument[] = [];
  employeesSharedCollection: IEmployee[] = [];

  protected paySlipService = inject(PaySlipService);
  protected paySlipFormService = inject(PaySlipFormService);
  protected documentService = inject(DocumentService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaySlipFormGroup = this.paySlipFormService.createPaySlipFormGroup();

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paySlip }) => {
      this.paySlip = paySlip;
      if (paySlip) {
        this.updateForm(paySlip);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paySlip = this.paySlipFormService.getPaySlip(this.editForm);
    if (paySlip.id !== null) {
      this.subscribeToSaveResponse(this.paySlipService.update(paySlip));
    } else {
      this.subscribeToSaveResponse(this.paySlipService.create(paySlip));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaySlip>>): void {
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

  protected updateForm(paySlip: IPaySlip): void {
    this.paySlip = paySlip;
    this.paySlipFormService.resetForm(this.editForm, paySlip);

    this.documentsCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(this.documentsCollection, paySlip.document);
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      paySlip.employee,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentService
      .query({ filter: 'payslip-is-null' })
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.paySlip?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsCollection = documents));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.paySlip?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
