import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmploymentContract } from '../employment-contract.model';
import { EmploymentContractService } from '../service/employment-contract.service';

@Component({
  standalone: true,
  templateUrl: './employment-contract-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmploymentContractDeleteDialogComponent {
  employmentContract?: IEmploymentContract;

  protected employmentContractService = inject(EmploymentContractService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employmentContractService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
