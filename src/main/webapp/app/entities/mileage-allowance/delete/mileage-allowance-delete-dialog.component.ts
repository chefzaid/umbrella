import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMileageAllowance } from '../mileage-allowance.model';
import { MileageAllowanceService } from '../service/mileage-allowance.service';

@Component({
  standalone: true,
  templateUrl: './mileage-allowance-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MileageAllowanceDeleteDialogComponent {
  mileageAllowance?: IMileageAllowance;

  protected mileageAllowanceService = inject(MileageAllowanceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mileageAllowanceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
