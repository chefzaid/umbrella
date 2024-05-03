import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceContract, NewServiceContract } from '../service-contract.model';

export type PartialUpdateServiceContract = Partial<IServiceContract> & Pick<IServiceContract, 'id'>;

type RestOf<T extends IServiceContract | NewServiceContract> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestServiceContract = RestOf<IServiceContract>;

export type NewRestServiceContract = RestOf<NewServiceContract>;

export type PartialUpdateRestServiceContract = RestOf<PartialUpdateServiceContract>;

export type EntityResponseType = HttpResponse<IServiceContract>;
export type EntityArrayResponseType = HttpResponse<IServiceContract[]>;

@Injectable({ providedIn: 'root' })
export class ServiceContractService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-contracts');

  create(serviceContract: NewServiceContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceContract);
    return this.http
      .post<RestServiceContract>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(serviceContract: IServiceContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceContract);
    return this.http
      .put<RestServiceContract>(`${this.resourceUrl}/${this.getServiceContractIdentifier(serviceContract)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(serviceContract: PartialUpdateServiceContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceContract);
    return this.http
      .patch<RestServiceContract>(`${this.resourceUrl}/${this.getServiceContractIdentifier(serviceContract)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestServiceContract>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestServiceContract[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getServiceContractIdentifier(serviceContract: Pick<IServiceContract, 'id'>): number {
    return serviceContract.id;
  }

  compareServiceContract(o1: Pick<IServiceContract, 'id'> | null, o2: Pick<IServiceContract, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceContractIdentifier(o1) === this.getServiceContractIdentifier(o2) : o1 === o2;
  }

  addServiceContractToCollectionIfMissing<Type extends Pick<IServiceContract, 'id'>>(
    serviceContractCollection: Type[],
    ...serviceContractsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceContracts: Type[] = serviceContractsToCheck.filter(isPresent);
    if (serviceContracts.length > 0) {
      const serviceContractCollectionIdentifiers = serviceContractCollection.map(serviceContractItem =>
        this.getServiceContractIdentifier(serviceContractItem),
      );
      const serviceContractsToAdd = serviceContracts.filter(serviceContractItem => {
        const serviceContractIdentifier = this.getServiceContractIdentifier(serviceContractItem);
        if (serviceContractCollectionIdentifiers.includes(serviceContractIdentifier)) {
          return false;
        }
        serviceContractCollectionIdentifiers.push(serviceContractIdentifier);
        return true;
      });
      return [...serviceContractsToAdd, ...serviceContractCollection];
    }
    return serviceContractCollection;
  }

  protected convertDateFromClient<T extends IServiceContract | NewServiceContract | PartialUpdateServiceContract>(
    serviceContract: T,
  ): RestOf<T> {
    return {
      ...serviceContract,
      startDate: serviceContract.startDate?.format(DATE_FORMAT) ?? null,
      endDate: serviceContract.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restServiceContract: RestServiceContract): IServiceContract {
    return {
      ...restServiceContract,
      startDate: restServiceContract.startDate ? dayjs(restServiceContract.startDate) : undefined,
      endDate: restServiceContract.endDate ? dayjs(restServiceContract.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestServiceContract>): HttpResponse<IServiceContract> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestServiceContract[]>): HttpResponse<IServiceContract[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
