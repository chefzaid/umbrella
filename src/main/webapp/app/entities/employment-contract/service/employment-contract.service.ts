import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmploymentContract, NewEmploymentContract } from '../employment-contract.model';

export type PartialUpdateEmploymentContract = Partial<IEmploymentContract> & Pick<IEmploymentContract, 'id'>;

type RestOf<T extends IEmploymentContract | NewEmploymentContract> = Omit<T, 'hireDate'> & {
  hireDate?: string | null;
};

export type RestEmploymentContract = RestOf<IEmploymentContract>;

export type NewRestEmploymentContract = RestOf<NewEmploymentContract>;

export type PartialUpdateRestEmploymentContract = RestOf<PartialUpdateEmploymentContract>;

export type EntityResponseType = HttpResponse<IEmploymentContract>;
export type EntityArrayResponseType = HttpResponse<IEmploymentContract[]>;

@Injectable({ providedIn: 'root' })
export class EmploymentContractService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employment-contracts');

  create(employmentContract: NewEmploymentContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentContract);
    return this.http
      .post<RestEmploymentContract>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(employmentContract: IEmploymentContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentContract);
    return this.http
      .put<RestEmploymentContract>(`${this.resourceUrl}/${this.getEmploymentContractIdentifier(employmentContract)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(employmentContract: PartialUpdateEmploymentContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentContract);
    return this.http
      .patch<RestEmploymentContract>(`${this.resourceUrl}/${this.getEmploymentContractIdentifier(employmentContract)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmploymentContract>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmploymentContract[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmploymentContractIdentifier(employmentContract: Pick<IEmploymentContract, 'id'>): number {
    return employmentContract.id;
  }

  compareEmploymentContract(o1: Pick<IEmploymentContract, 'id'> | null, o2: Pick<IEmploymentContract, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmploymentContractIdentifier(o1) === this.getEmploymentContractIdentifier(o2) : o1 === o2;
  }

  addEmploymentContractToCollectionIfMissing<Type extends Pick<IEmploymentContract, 'id'>>(
    employmentContractCollection: Type[],
    ...employmentContractsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employmentContracts: Type[] = employmentContractsToCheck.filter(isPresent);
    if (employmentContracts.length > 0) {
      const employmentContractCollectionIdentifiers = employmentContractCollection.map(employmentContractItem =>
        this.getEmploymentContractIdentifier(employmentContractItem),
      );
      const employmentContractsToAdd = employmentContracts.filter(employmentContractItem => {
        const employmentContractIdentifier = this.getEmploymentContractIdentifier(employmentContractItem);
        if (employmentContractCollectionIdentifiers.includes(employmentContractIdentifier)) {
          return false;
        }
        employmentContractCollectionIdentifiers.push(employmentContractIdentifier);
        return true;
      });
      return [...employmentContractsToAdd, ...employmentContractCollection];
    }
    return employmentContractCollection;
  }

  protected convertDateFromClient<T extends IEmploymentContract | NewEmploymentContract | PartialUpdateEmploymentContract>(
    employmentContract: T,
  ): RestOf<T> {
    return {
      ...employmentContract,
      hireDate: employmentContract.hireDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEmploymentContract: RestEmploymentContract): IEmploymentContract {
    return {
      ...restEmploymentContract,
      hireDate: restEmploymentContract.hireDate ? dayjs(restEmploymentContract.hireDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmploymentContract>): HttpResponse<IEmploymentContract> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmploymentContract[]>): HttpResponse<IEmploymentContract[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
