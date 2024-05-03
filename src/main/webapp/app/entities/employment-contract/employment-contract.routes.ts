import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EmploymentContractComponent } from './list/employment-contract.component';
import { EmploymentContractDetailComponent } from './detail/employment-contract-detail.component';
import { EmploymentContractUpdateComponent } from './update/employment-contract-update.component';
import EmploymentContractResolve from './route/employment-contract-routing-resolve.service';

const employmentContractRoute: Routes = [
  {
    path: '',
    component: EmploymentContractComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmploymentContractDetailComponent,
    resolve: {
      employmentContract: EmploymentContractResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmploymentContractUpdateComponent,
    resolve: {
      employmentContract: EmploymentContractResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmploymentContractUpdateComponent,
    resolve: {
      employmentContract: EmploymentContractResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default employmentContractRoute;
