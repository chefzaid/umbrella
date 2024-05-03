import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IExpenseType } from '../expense-type.model';

@Component({
  standalone: true,
  selector: 'jhi-expense-type-detail',
  templateUrl: './expense-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ExpenseTypeDetailComponent {
  expenseType = input<IExpenseType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
