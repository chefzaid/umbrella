import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaySlip } from '../pay-slip.model';
import { PaySlipService } from '../service/pay-slip.service';

const paySlipResolve = (route: ActivatedRouteSnapshot): Observable<null | IPaySlip> => {
  const id = route.params['id'];
  if (id) {
    return inject(PaySlipService)
      .find(id)
      .pipe(
        mergeMap((paySlip: HttpResponse<IPaySlip>) => {
          if (paySlip.body) {
            return of(paySlip.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default paySlipResolve;
