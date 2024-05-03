import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ActivityReportComponent } from './list/activity-report.component';
import { ActivityReportDetailComponent } from './detail/activity-report-detail.component';
import { ActivityReportUpdateComponent } from './update/activity-report-update.component';
import ActivityReportResolve from './route/activity-report-routing-resolve.service';

const activityReportRoute: Routes = [
  {
    path: '',
    component: ActivityReportComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActivityReportDetailComponent,
    resolve: {
      activityReport: ActivityReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ActivityReportUpdateComponent,
    resolve: {
      activityReport: ActivityReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ActivityReportUpdateComponent,
    resolve: {
      activityReport: ActivityReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default activityReportRoute;
