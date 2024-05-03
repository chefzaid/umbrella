import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITimeSheetLine } from '../time-sheet-line.model';
import { TimeSheetLineService } from '../service/time-sheet-line.service';

const timeSheetLineResolve = (route: ActivatedRouteSnapshot): Observable<null | ITimeSheetLine> => {
  const id = route.params['id'];
  if (id) {
    return inject(TimeSheetLineService)
      .find(id)
      .pipe(
        mergeMap((timeSheetLine: HttpResponse<ITimeSheetLine>) => {
          if (timeSheetLine.body) {
            return of(timeSheetLine.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default timeSheetLineResolve;
