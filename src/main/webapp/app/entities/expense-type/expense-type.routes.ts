import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ExpenseTypeComponent } from './list/expense-type.component';
import { ExpenseTypeDetailComponent } from './detail/expense-type-detail.component';
import { ExpenseTypeUpdateComponent } from './update/expense-type-update.component';
import ExpenseTypeResolve from './route/expense-type-routing-resolve.service';

const expenseTypeRoute: Routes = [
  {
    path: '',
    component: ExpenseTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExpenseTypeDetailComponent,
    resolve: {
      expenseType: ExpenseTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExpenseTypeUpdateComponent,
    resolve: {
      expenseType: ExpenseTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExpenseTypeUpdateComponent,
    resolve: {
      expenseType: ExpenseTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default expenseTypeRoute;
