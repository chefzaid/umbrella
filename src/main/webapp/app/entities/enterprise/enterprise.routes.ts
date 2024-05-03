import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EnterpriseComponent } from './list/enterprise.component';
import { EnterpriseDetailComponent } from './detail/enterprise-detail.component';
import { EnterpriseUpdateComponent } from './update/enterprise-update.component';
import EnterpriseResolve from './route/enterprise-routing-resolve.service';

const enterpriseRoute: Routes = [
  {
    path: '',
    component: EnterpriseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EnterpriseDetailComponent,
    resolve: {
      enterprise: EnterpriseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EnterpriseUpdateComponent,
    resolve: {
      enterprise: EnterpriseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EnterpriseUpdateComponent,
    resolve: {
      enterprise: EnterpriseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default enterpriseRoute;
