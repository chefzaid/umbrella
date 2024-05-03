import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExpenseType } from '../expense-type.model';
import { ExpenseTypeService } from '../service/expense-type.service';

@Component({
  standalone: true,
  templateUrl: './expense-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExpenseTypeDeleteDialogComponent {
  expenseType?: IExpenseType;

  protected expenseTypeService = inject(ExpenseTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
