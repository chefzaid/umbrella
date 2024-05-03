import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IActivityReport } from '../activity-report.model';
import { ActivityReportService } from '../service/activity-report.service';

const activityReportResolve = (route: ActivatedRouteSnapshot): Observable<null | IActivityReport> => {
  const id = route.params['id'];
  if (id) {
    return inject(ActivityReportService)
      .find(id)
      .pipe(
        mergeMap((activityReport: HttpResponse<IActivityReport>) => {
          if (activityReport.body) {
            return of(activityReport.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default activityReportResolve;
