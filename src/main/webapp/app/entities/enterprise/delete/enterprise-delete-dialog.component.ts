import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEnterprise } from '../enterprise.model';
import { EnterpriseService } from '../service/enterprise.service';

@Component({
  standalone: true,
  templateUrl: './enterprise-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EnterpriseDeleteDialogComponent {
  enterprise?: IEnterprise;

  protected enterpriseService = inject(EnterpriseService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.enterpriseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
