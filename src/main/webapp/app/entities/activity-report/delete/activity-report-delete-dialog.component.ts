import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IActivityReport } from '../activity-report.model';
import { ActivityReportService } from '../service/activity-report.service';

@Component({
  standalone: true,
  templateUrl: './activity-report-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ActivityReportDeleteDialogComponent {
  activityReport?: IActivityReport;

  protected activityReportService = inject(ActivityReportService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.activityReportService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
