import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MileageAllowanceComponent } from './list/mileage-allowance.component';
import { MileageAllowanceDetailComponent } from './detail/mileage-allowance-detail.component';
import { MileageAllowanceUpdateComponent } from './update/mileage-allowance-update.component';
import MileageAllowanceResolve from './route/mileage-allowance-routing-resolve.service';

const mileageAllowanceRoute: Routes = [
  {
    path: '',
    component: MileageAllowanceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MileageAllowanceDetailComponent,
    resolve: {
      mileageAllowance: MileageAllowanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MileageAllowanceUpdateComponent,
    resolve: {
      mileageAllowance: MileageAllowanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MileageAllowanceUpdateComponent,
    resolve: {
      mileageAllowance: MileageAllowanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default mileageAllowanceRoute;
