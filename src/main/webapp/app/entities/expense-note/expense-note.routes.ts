import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ExpenseNoteComponent } from './list/expense-note.component';
import { ExpenseNoteDetailComponent } from './detail/expense-note-detail.component';
import { ExpenseNoteUpdateComponent } from './update/expense-note-update.component';
import ExpenseNoteResolve from './route/expense-note-routing-resolve.service';

const expenseNoteRoute: Routes = [
  {
    path: '',
    component: ExpenseNoteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExpenseNoteDetailComponent,
    resolve: {
      expenseNote: ExpenseNoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExpenseNoteUpdateComponent,
    resolve: {
      expenseNote: ExpenseNoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExpenseNoteUpdateComponent,
    resolve: {
      expenseNote: ExpenseNoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default expenseNoteRoute;
