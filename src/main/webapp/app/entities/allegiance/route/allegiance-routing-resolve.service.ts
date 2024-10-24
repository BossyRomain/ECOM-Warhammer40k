import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAllegiance } from '../allegiance.model';
import { AllegianceService } from '../service/allegiance.service';

const allegianceResolve = (route: ActivatedRouteSnapshot): Observable<null | IAllegiance> => {
  const id = route.params.id;
  if (id) {
    return inject(AllegianceService)
      .find(id)
      .pipe(
        mergeMap((allegiance: HttpResponse<IAllegiance>) => {
          if (allegiance.body) {
            return of(allegiance.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default allegianceResolve;
