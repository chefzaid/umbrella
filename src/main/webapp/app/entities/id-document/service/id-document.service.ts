import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIdDocument, NewIdDocument } from '../id-document.model';

export type PartialUpdateIdDocument = Partial<IIdDocument> & Pick<IIdDocument, 'id'>;

export type EntityResponseType = HttpResponse<IIdDocument>;
export type EntityArrayResponseType = HttpResponse<IIdDocument[]>;

@Injectable({ providedIn: 'root' })
export class IdDocumentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/id-documents');

  create(idDocument: NewIdDocument): Observable<EntityResponseType> {
    return this.http.post<IIdDocument>(this.resourceUrl, idDocument, { observe: 'response' });
  }

  update(idDocument: IIdDocument): Observable<EntityResponseType> {
    return this.http.put<IIdDocument>(`${this.resourceUrl}/${this.getIdDocumentIdentifier(idDocument)}`, idDocument, {
      observe: 'response',
    });
  }

  partialUpdate(idDocument: PartialUpdateIdDocument): Observable<EntityResponseType> {
    return this.http.patch<IIdDocument>(`${this.resourceUrl}/${this.getIdDocumentIdentifier(idDocument)}`, idDocument, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIdDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIdDocument[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIdDocumentIdentifier(idDocument: Pick<IIdDocument, 'id'>): number {
    return idDocument.id;
  }

  compareIdDocument(o1: Pick<IIdDocument, 'id'> | null, o2: Pick<IIdDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getIdDocumentIdentifier(o1) === this.getIdDocumentIdentifier(o2) : o1 === o2;
  }

  addIdDocumentToCollectionIfMissing<Type extends Pick<IIdDocument, 'id'>>(
    idDocumentCollection: Type[],
    ...idDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const idDocuments: Type[] = idDocumentsToCheck.filter(isPresent);
    if (idDocuments.length > 0) {
      const idDocumentCollectionIdentifiers = idDocumentCollection.map(idDocumentItem => this.getIdDocumentIdentifier(idDocumentItem));
      const idDocumentsToAdd = idDocuments.filter(idDocumentItem => {
        const idDocumentIdentifier = this.getIdDocumentIdentifier(idDocumentItem);
        if (idDocumentCollectionIdentifiers.includes(idDocumentIdentifier)) {
          return false;
        }
        idDocumentCollectionIdentifiers.push(idDocumentIdentifier);
        return true;
      });
      return [...idDocumentsToAdd, ...idDocumentCollection];
    }
    return idDocumentCollection;
  }
}
