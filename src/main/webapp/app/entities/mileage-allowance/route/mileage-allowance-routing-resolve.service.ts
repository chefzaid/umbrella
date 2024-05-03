import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMileageAllowance } from '../mileage-allowance.model';
import { MileageAllowanceService } from '../service/mileage-allowance.service';

const mileageAllowanceResolve = (route: ActivatedRouteSnapshot): Observable<null | IMileageAllowance> => {
  const id = route.params['id'];
  if (id) {
    return inject(MileageAllowanceService)
      .find(id)
      .pipe(
        mergeMap((mileageAllowance: HttpResponse<IMileageAllowance>) => {
          if (mileageAllowance.body) {
            return of(mileageAllowance.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default mileageAllowanceResolve;
