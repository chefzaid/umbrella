import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITimeSheet } from '../time-sheet.model';
import { TimeSheetService } from '../service/time-sheet.service';

const timeSheetResolve = (route: ActivatedRouteSnapshot): Observable<null | ITimeSheet> => {
  const id = route.params['id'];
  if (id) {
    return inject(TimeSheetService)
      .find(id)
      .pipe(
        mergeMap((timeSheet: HttpResponse<ITimeSheet>) => {
          if (timeSheet.body) {
            return of(timeSheet.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default timeSheetResolve;
