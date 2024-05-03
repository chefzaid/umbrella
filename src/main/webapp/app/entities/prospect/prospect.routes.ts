import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProspectComponent } from './list/prospect.component';
import { ProspectDetailComponent } from './detail/prospect-detail.component';
import { ProspectUpdateComponent } from './update/prospect-update.component';
import ProspectResolve from './route/prospect-routing-resolve.service';

const prospectRoute: Routes = [
  {
    path: '',
    component: ProspectComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProspectDetailComponent,
    resolve: {
      prospect: ProspectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProspectUpdateComponent,
    resolve: {
      prospect: ProspectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProspectUpdateComponent,
    resolve: {
      prospect: ProspectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default prospectRoute;
