import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TimeSheetLineComponent } from './list/time-sheet-line.component';
import { TimeSheetLineDetailComponent } from './detail/time-sheet-line-detail.component';
import { TimeSheetLineUpdateComponent } from './update/time-sheet-line-update.component';
import TimeSheetLineResolve from './route/time-sheet-line-routing-resolve.service';

const timeSheetLineRoute: Routes = [
  {
    path: '',
    component: TimeSheetLineComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TimeSheetLineDetailComponent,
    resolve: {
      timeSheetLine: TimeSheetLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TimeSheetLineUpdateComponent,
    resolve: {
      timeSheetLine: TimeSheetLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TimeSheetLineUpdateComponent,
    resolve: {
      timeSheetLine: TimeSheetLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default timeSheetLineRoute;
