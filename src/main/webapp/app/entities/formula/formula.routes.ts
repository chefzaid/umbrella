import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FormulaComponent } from './list/formula.component';
import { FormulaDetailComponent } from './detail/formula-detail.component';
import { FormulaUpdateComponent } from './update/formula-update.component';
import FormulaResolve from './route/formula-routing-resolve.service';

const formulaRoute: Routes = [
  {
    path: '',
    component: FormulaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormulaDetailComponent,
    resolve: {
      formula: FormulaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormulaUpdateComponent,
    resolve: {
      formula: FormulaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormulaUpdateComponent,
    resolve: {
      formula: FormulaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default formulaRoute;
