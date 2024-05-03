import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PaySlipComponent } from './list/pay-slip.component';
import { PaySlipDetailComponent } from './detail/pay-slip-detail.component';
import { PaySlipUpdateComponent } from './update/pay-slip-update.component';
import PaySlipResolve from './route/pay-slip-routing-resolve.service';

const paySlipRoute: Routes = [
  {
    path: '',
    component: PaySlipComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaySlipDetailComponent,
    resolve: {
      paySlip: PaySlipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaySlipUpdateComponent,
    resolve: {
      paySlip: PaySlipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaySlipUpdateComponent,
    resolve: {
      paySlip: PaySlipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default paySlipRoute;
