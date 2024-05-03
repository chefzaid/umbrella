import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEnterprise } from '../enterprise.model';
import { EnterpriseService } from '../service/enterprise.service';

const enterpriseResolve = (route: ActivatedRouteSnapshot): Observable<null | IEnterprise> => {
  const id = route.params['id'];
  if (id) {
    return inject(EnterpriseService)
      .find(id)
      .pipe(
        mergeMap((enterprise: HttpResponse<IEnterprise>) => {
          if (enterprise.body) {
            return of(enterprise.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default enterpriseResolve;
