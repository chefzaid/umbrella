import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITimeSheetLine } from '../time-sheet-line.model';
import { TimeSheetLineService } from '../service/time-sheet-line.service';

@Component({
  standalone: true,
  templateUrl: './time-sheet-line-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TimeSheetLineDeleteDialogComponent {
  timeSheetLine?: ITimeSheetLine;

  protected timeSheetLineService = inject(TimeSheetLineService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.timeSheetLineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
