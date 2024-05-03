import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IServiceContract } from '../service-contract.model';
import { ServiceContractService } from '../service/service-contract.service';

@Component({
  standalone: true,
  templateUrl: './service-contract-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ServiceContractDeleteDialogComponent {
  serviceContract?: IServiceContract;

  protected serviceContractService = inject(ServiceContractService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceContractService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
