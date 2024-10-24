import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommandLine, NewCommandLine } from '../command-line.model';

export type PartialUpdateCommandLine = Partial<ICommandLine> & Pick<ICommandLine, 'id'>;

export type EntityResponseType = HttpResponse<ICommandLine>;
export type EntityArrayResponseType = HttpResponse<ICommandLine[]>;

@Injectable({ providedIn: 'root' })
export class CommandLineService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/command-lines');

  create(commandLine: NewCommandLine): Observable<EntityResponseType> {
    return this.http.post<ICommandLine>(this.resourceUrl, commandLine, { observe: 'response' });
  }

  update(commandLine: ICommandLine): Observable<EntityResponseType> {
    return this.http.put<ICommandLine>(`${this.resourceUrl}/${this.getCommandLineIdentifier(commandLine)}`, commandLine, {
      observe: 'response',
    });
  }

  partialUpdate(commandLine: PartialUpdateCommandLine): Observable<EntityResponseType> {
    return this.http.patch<ICommandLine>(`${this.resourceUrl}/${this.getCommandLineIdentifier(commandLine)}`, commandLine, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommandLine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommandLine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCommandLineIdentifier(commandLine: Pick<ICommandLine, 'id'>): number {
    return commandLine.id;
  }

  compareCommandLine(o1: Pick<ICommandLine, 'id'> | null, o2: Pick<ICommandLine, 'id'> | null): boolean {
    return o1 && o2 ? this.getCommandLineIdentifier(o1) === this.getCommandLineIdentifier(o2) : o1 === o2;
  }

  addCommandLineToCollectionIfMissing<Type extends Pick<ICommandLine, 'id'>>(
    commandLineCollection: Type[],
    ...commandLinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const commandLines: Type[] = commandLinesToCheck.filter(isPresent);
    if (commandLines.length > 0) {
      const commandLineCollectionIdentifiers = commandLineCollection.map(commandLineItem => this.getCommandLineIdentifier(commandLineItem));
      const commandLinesToAdd = commandLines.filter(commandLineItem => {
        const commandLineIdentifier = this.getCommandLineIdentifier(commandLineItem);
        if (commandLineCollectionIdentifiers.includes(commandLineIdentifier)) {
          return false;
        }
        commandLineCollectionIdentifiers.push(commandLineIdentifier);
        return true;
      });
      return [...commandLinesToAdd, ...commandLineCollection];
    }
    return commandLineCollection;
  }
}
