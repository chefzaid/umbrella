import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExpenseNote } from '../expense-note.model';
import { ExpenseNoteService } from '../service/expense-note.service';

@Component({
  standalone: true,
  templateUrl: './expense-note-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExpenseNoteDeleteDialogComponent {
  expenseNote?: IExpenseNote;

  protected expenseNoteService = inject(ExpenseNoteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseNoteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
