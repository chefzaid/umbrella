import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmploymentContract } from '../employment-contract.model';
import { EmploymentContractService } from '../service/employment-contract.service';

const employmentContractResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmploymentContract> => {
  const id = route.params['id'];
  if (id) {
    return inject(EmploymentContractService)
      .find(id)
      .pipe(
        mergeMap((employmentContract: HttpResponse<IEmploymentContract>) => {
          if (employmentContract.body) {
            return of(employmentContract.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default employmentContractResolve;
