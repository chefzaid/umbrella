import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TimeSheetComponent } from './list/time-sheet.component';
import { TimeSheetDetailComponent } from './detail/time-sheet-detail.component';
import { TimeSheetUpdateComponent } from './update/time-sheet-update.component';
import TimeSheetResolve from './route/time-sheet-routing-resolve.service';

const timeSheetRoute: Routes = [
  {
    path: '',
    component: TimeSheetComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TimeSheetDetailComponent,
    resolve: {
      timeSheet: TimeSheetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TimeSheetUpdateComponent,
    resolve: {
      timeSheet: TimeSheetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TimeSheetUpdateComponent,
    resolve: {
      timeSheet: TimeSheetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default timeSheetRoute;
