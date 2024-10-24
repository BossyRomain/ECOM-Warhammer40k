import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommandLine } from '../command-line.model';
import { CommandLineService } from '../service/command-line.service';

const commandLineResolve = (route: ActivatedRouteSnapshot): Observable<null | ICommandLine> => {
  const id = route.params.id;
  if (id) {
    return inject(CommandLineService)
      .find(id)
      .pipe(
        mergeMap((commandLine: HttpResponse<ICommandLine>) => {
          if (commandLine.body) {
            return of(commandLine.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default commandLineResolve;
