import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ParameterGroupComponent } from './list/parameter-group.component';
import { ParameterGroupDetailComponent } from './detail/parameter-group-detail.component';
import { ParameterGroupUpdateComponent } from './update/parameter-group-update.component';
import ParameterGroupResolve from './route/parameter-group-routing-resolve.service';

const parameterGroupRoute: Routes = [
  {
    path: '',
    component: ParameterGroupComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParameterGroupDetailComponent,
    resolve: {
      parameterGroup: ParameterGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParameterGroupUpdateComponent,
    resolve: {
      parameterGroup: ParameterGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParameterGroupUpdateComponent,
    resolve: {
      parameterGroup: ParameterGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default parameterGroupRoute;
