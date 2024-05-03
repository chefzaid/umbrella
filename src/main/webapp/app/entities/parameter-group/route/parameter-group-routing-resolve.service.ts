import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParameterGroup } from '../parameter-group.model';
import { ParameterGroupService } from '../service/parameter-group.service';

const parameterGroupResolve = (route: ActivatedRouteSnapshot): Observable<null | IParameterGroup> => {
  const id = route.params['id'];
  if (id) {
    return inject(ParameterGroupService)
      .find(id)
      .pipe(
        mergeMap((parameterGroup: HttpResponse<IParameterGroup>) => {
          if (parameterGroup.body) {
            return of(parameterGroup.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default parameterGroupResolve;
