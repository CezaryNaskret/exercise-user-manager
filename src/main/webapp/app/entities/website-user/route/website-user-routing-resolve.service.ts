import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWebsiteUser, WebsiteUser } from '../website-user.model';
import { WebsiteUserService } from '../service/website-user.service';

@Injectable({ providedIn: 'root' })
export class WebsiteUserRoutingResolveService implements Resolve<IWebsiteUser> {
  constructor(protected service: WebsiteUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWebsiteUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((websiteUser: HttpResponse<WebsiteUser>) => {
          if (websiteUser.body) {
            return of(websiteUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WebsiteUser());
  }
}
