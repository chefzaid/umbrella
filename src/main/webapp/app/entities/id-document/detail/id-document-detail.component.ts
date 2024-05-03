import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IIdDocument } from '../id-document.model';

@Component({
  standalone: true,
  selector: 'jhi-id-document-detail',
  templateUrl: './id-document-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class IdDocumentDetailComponent {
  idDocument = input<IIdDocument | null>(null);

  previousState(): void {
    window.history.back();
  }
}
