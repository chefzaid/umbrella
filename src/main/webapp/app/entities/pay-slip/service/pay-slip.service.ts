import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaySlip, NewPaySlip } from '../pay-slip.model';

export type PartialUpdatePaySlip = Partial<IPaySlip> & Pick<IPaySlip, 'id'>;

export type EntityResponseType = HttpResponse<IPaySlip>;
export type EntityArrayResponseType = HttpResponse<IPaySlip[]>;

@Injectable({ providedIn: 'root' })
export class PaySlipService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pay-slips');

  create(paySlip: NewPaySlip): Observable<EntityResponseType> {
    return this.http.post<IPaySlip>(this.resourceUrl, paySlip, { observe: 'response' });
  }

  update(paySlip: IPaySlip): Observable<EntityResponseType> {
    return this.http.put<IPaySlip>(`${this.resourceUrl}/${this.getPaySlipIdentifier(paySlip)}`, paySlip, { observe: 'response' });
  }

  partialUpdate(paySlip: PartialUpdatePaySlip): Observable<EntityResponseType> {
    return this.http.patch<IPaySlip>(`${this.resourceUrl}/${this.getPaySlipIdentifier(paySlip)}`, paySlip, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaySlip>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaySlip[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPaySlipIdentifier(paySlip: Pick<IPaySlip, 'id'>): number {
    return paySlip.id;
  }

  comparePaySlip(o1: Pick<IPaySlip, 'id'> | null, o2: Pick<IPaySlip, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaySlipIdentifier(o1) === this.getPaySlipIdentifier(o2) : o1 === o2;
  }

  addPaySlipToCollectionIfMissing<Type extends Pick<IPaySlip, 'id'>>(
    paySlipCollection: Type[],
    ...paySlipsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paySlips: Type[] = paySlipsToCheck.filter(isPresent);
    if (paySlips.length > 0) {
      const paySlipCollectionIdentifiers = paySlipCollection.map(paySlipItem => this.getPaySlipIdentifier(paySlipItem));
      const paySlipsToAdd = paySlips.filter(paySlipItem => {
        const paySlipIdentifier = this.getPaySlipIdentifier(paySlipItem);
        if (paySlipCollectionIdentifiers.includes(paySlipIdentifier)) {
          return false;
        }
        paySlipCollectionIdentifiers.push(paySlipIdentifier);
        return true;
      });
      return [...paySlipsToAdd, ...paySlipCollection];
    }
    return paySlipCollection;
  }
}
