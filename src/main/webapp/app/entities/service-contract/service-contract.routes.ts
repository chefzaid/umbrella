import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ServiceContractComponent } from './list/service-contract.component';
import { ServiceContractDetailComponent } from './detail/service-contract-detail.component';
import { ServiceContractUpdateComponent } from './update/service-contract-update.component';
import ServiceContractResolve from './route/service-contract-routing-resolve.service';

const serviceContractRoute: Routes = [
  {
    path: '',
    component: ServiceContractComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceContractDetailComponent,
    resolve: {
      serviceContract: ServiceContractResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceContractUpdateComponent,
    resolve: {
      serviceContract: ServiceContractResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceContractUpdateComponent,
    resolve: {
      serviceContract: ServiceContractResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default serviceContractRoute;
