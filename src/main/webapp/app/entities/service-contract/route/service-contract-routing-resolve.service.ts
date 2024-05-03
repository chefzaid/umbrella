import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceContract } from '../service-contract.model';
import { ServiceContractService } from '../service/service-contract.service';

const serviceContractResolve = (route: ActivatedRouteSnapshot): Observable<null | IServiceContract> => {
  const id = route.params['id'];
  if (id) {
    return inject(ServiceContractService)
      .find(id)
      .pipe(
        mergeMap((serviceContract: HttpResponse<IServiceContract>) => {
          if (serviceContract.body) {
            return of(serviceContract.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default serviceContractResolve;
