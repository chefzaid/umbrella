import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMileageAllowance, NewMileageAllowance } from '../mileage-allowance.model';

export type PartialUpdateMileageAllowance = Partial<IMileageAllowance> & Pick<IMileageAllowance, 'id'>;

export type EntityResponseType = HttpResponse<IMileageAllowance>;
export type EntityArrayResponseType = HttpResponse<IMileageAllowance[]>;

@Injectable({ providedIn: 'root' })
export class MileageAllowanceService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mileage-allowances');

  create(mileageAllowance: NewMileageAllowance): Observable<EntityResponseType> {
    return this.http.post<IMileageAllowance>(this.resourceUrl, mileageAllowance, { observe: 'response' });
  }

  update(mileageAllowance: IMileageAllowance): Observable<EntityResponseType> {
    return this.http.put<IMileageAllowance>(
      `${this.resourceUrl}/${this.getMileageAllowanceIdentifier(mileageAllowance)}`,
      mileageAllowance,
      { observe: 'response' },
    );
  }

  partialUpdate(mileageAllowance: PartialUpdateMileageAllowance): Observable<EntityResponseType> {
    return this.http.patch<IMileageAllowance>(
      `${this.resourceUrl}/${this.getMileageAllowanceIdentifier(mileageAllowance)}`,
      mileageAllowance,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMileageAllowance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMileageAllowance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMileageAllowanceIdentifier(mileageAllowance: Pick<IMileageAllowance, 'id'>): number {
    return mileageAllowance.id;
  }

  compareMileageAllowance(o1: Pick<IMileageAllowance, 'id'> | null, o2: Pick<IMileageAllowance, 'id'> | null): boolean {
    return o1 && o2 ? this.getMileageAllowanceIdentifier(o1) === this.getMileageAllowanceIdentifier(o2) : o1 === o2;
  }

  addMileageAllowanceToCollectionIfMissing<Type extends Pick<IMileageAllowance, 'id'>>(
    mileageAllowanceCollection: Type[],
    ...mileageAllowancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mileageAllowances: Type[] = mileageAllowancesToCheck.filter(isPresent);
    if (mileageAllowances.length > 0) {
      const mileageAllowanceCollectionIdentifiers = mileageAllowanceCollection.map(mileageAllowanceItem =>
        this.getMileageAllowanceIdentifier(mileageAllowanceItem),
      );
      const mileageAllowancesToAdd = mileageAllowances.filter(mileageAllowanceItem => {
        const mileageAllowanceIdentifier = this.getMileageAllowanceIdentifier(mileageAllowanceItem);
        if (mileageAllowanceCollectionIdentifiers.includes(mileageAllowanceIdentifier)) {
          return false;
        }
        mileageAllowanceCollectionIdentifiers.push(mileageAllowanceIdentifier);
        return true;
      });
      return [...mileageAllowancesToAdd, ...mileageAllowanceCollection];
    }
    return mileageAllowanceCollection;
  }
}
