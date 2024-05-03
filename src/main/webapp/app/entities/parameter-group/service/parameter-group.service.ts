import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParameterGroup, NewParameterGroup } from '../parameter-group.model';

export type PartialUpdateParameterGroup = Partial<IParameterGroup> & Pick<IParameterGroup, 'id'>;

export type EntityResponseType = HttpResponse<IParameterGroup>;
export type EntityArrayResponseType = HttpResponse<IParameterGroup[]>;

@Injectable({ providedIn: 'root' })
export class ParameterGroupService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parameter-groups');

  create(parameterGroup: NewParameterGroup): Observable<EntityResponseType> {
    return this.http.post<IParameterGroup>(this.resourceUrl, parameterGroup, { observe: 'response' });
  }

  update(parameterGroup: IParameterGroup): Observable<EntityResponseType> {
    return this.http.put<IParameterGroup>(`${this.resourceUrl}/${this.getParameterGroupIdentifier(parameterGroup)}`, parameterGroup, {
      observe: 'response',
    });
  }

  partialUpdate(parameterGroup: PartialUpdateParameterGroup): Observable<EntityResponseType> {
    return this.http.patch<IParameterGroup>(`${this.resourceUrl}/${this.getParameterGroupIdentifier(parameterGroup)}`, parameterGroup, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParameterGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParameterGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getParameterGroupIdentifier(parameterGroup: Pick<IParameterGroup, 'id'>): number {
    return parameterGroup.id;
  }

  compareParameterGroup(o1: Pick<IParameterGroup, 'id'> | null, o2: Pick<IParameterGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getParameterGroupIdentifier(o1) === this.getParameterGroupIdentifier(o2) : o1 === o2;
  }

  addParameterGroupToCollectionIfMissing<Type extends Pick<IParameterGroup, 'id'>>(
    parameterGroupCollection: Type[],
    ...parameterGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const parameterGroups: Type[] = parameterGroupsToCheck.filter(isPresent);
    if (parameterGroups.length > 0) {
      const parameterGroupCollectionIdentifiers = parameterGroupCollection.map(parameterGroupItem =>
        this.getParameterGroupIdentifier(parameterGroupItem),
      );
      const parameterGroupsToAdd = parameterGroups.filter(parameterGroupItem => {
        const parameterGroupIdentifier = this.getParameterGroupIdentifier(parameterGroupItem);
        if (parameterGroupCollectionIdentifiers.includes(parameterGroupIdentifier)) {
          return false;
        }
        parameterGroupCollectionIdentifiers.push(parameterGroupIdentifier);
        return true;
      });
      return [...parameterGroupsToAdd, ...parameterGroupCollection];
    }
    return parameterGroupCollection;
  }
}
