import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAllegiance, NewAllegiance } from '../allegiance.model';

export type PartialUpdateAllegiance = Partial<IAllegiance> & Pick<IAllegiance, 'id'>;

export type EntityResponseType = HttpResponse<IAllegiance>;
export type EntityArrayResponseType = HttpResponse<IAllegiance[]>;

@Injectable({ providedIn: 'root' })
export class AllegianceService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/allegiances');

  create(allegiance: NewAllegiance): Observable<EntityResponseType> {
    return this.http.post<IAllegiance>(this.resourceUrl, allegiance, { observe: 'response' });
  }

  update(allegiance: IAllegiance): Observable<EntityResponseType> {
    return this.http.put<IAllegiance>(`${this.resourceUrl}/${this.getAllegianceIdentifier(allegiance)}`, allegiance, {
      observe: 'response',
    });
  }

  partialUpdate(allegiance: PartialUpdateAllegiance): Observable<EntityResponseType> {
    return this.http.patch<IAllegiance>(`${this.resourceUrl}/${this.getAllegianceIdentifier(allegiance)}`, allegiance, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAllegiance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAllegiance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAllegianceIdentifier(allegiance: Pick<IAllegiance, 'id'>): number {
    return allegiance.id;
  }

  compareAllegiance(o1: Pick<IAllegiance, 'id'> | null, o2: Pick<IAllegiance, 'id'> | null): boolean {
    return o1 && o2 ? this.getAllegianceIdentifier(o1) === this.getAllegianceIdentifier(o2) : o1 === o2;
  }

  addAllegianceToCollectionIfMissing<Type extends Pick<IAllegiance, 'id'>>(
    allegianceCollection: Type[],
    ...allegiancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const allegiances: Type[] = allegiancesToCheck.filter(isPresent);
    if (allegiances.length > 0) {
      const allegianceCollectionIdentifiers = allegianceCollection.map(allegianceItem => this.getAllegianceIdentifier(allegianceItem));
      const allegiancesToAdd = allegiances.filter(allegianceItem => {
        const allegianceIdentifier = this.getAllegianceIdentifier(allegianceItem);
        if (allegianceCollectionIdentifiers.includes(allegianceIdentifier)) {
          return false;
        }
        allegianceCollectionIdentifiers.push(allegianceIdentifier);
        return true;
      });
      return [...allegiancesToAdd, ...allegianceCollection];
    }
    return allegianceCollection;
  }
}
