import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IIdDocument } from '../id-document.model';
import { IdDocumentService } from '../service/id-document.service';

@Component({
  standalone: true,
  templateUrl: './id-document-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class IdDocumentDeleteDialogComponent {
  idDocument?: IIdDocument;

  protected idDocumentService = inject(IdDocumentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.idDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
