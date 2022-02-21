import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWebsiteUser, getWebsiteUserIdentifier } from '../website-user.model';

export type EntityResponseType = HttpResponse<IWebsiteUser>;
export type EntityArrayResponseType = HttpResponse<IWebsiteUser[]>;

@Injectable({ providedIn: 'root' })
export class WebsiteUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/website-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(websiteUser: IWebsiteUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(websiteUser);
    return this.http
      .post<IWebsiteUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(websiteUser: IWebsiteUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(websiteUser);
    return this.http
      .put<IWebsiteUser>(`${this.resourceUrl}/${getWebsiteUserIdentifier(websiteUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(websiteUser: IWebsiteUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(websiteUser);
    return this.http
      .patch<IWebsiteUser>(`${this.resourceUrl}/${getWebsiteUserIdentifier(websiteUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWebsiteUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWebsiteUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWebsiteUserToCollectionIfMissing(
    websiteUserCollection: IWebsiteUser[],
    ...websiteUsersToCheck: (IWebsiteUser | null | undefined)[]
  ): IWebsiteUser[] {
    const websiteUsers: IWebsiteUser[] = websiteUsersToCheck.filter(isPresent);
    if (websiteUsers.length > 0) {
      const websiteUserCollectionIdentifiers = websiteUserCollection.map(websiteUserItem => getWebsiteUserIdentifier(websiteUserItem)!);
      const websiteUsersToAdd = websiteUsers.filter(websiteUserItem => {
        const websiteUserIdentifier = getWebsiteUserIdentifier(websiteUserItem);
        if (websiteUserIdentifier == null || websiteUserCollectionIdentifiers.includes(websiteUserIdentifier)) {
          return false;
        }
        websiteUserCollectionIdentifiers.push(websiteUserIdentifier);
        return true;
      });
      return [...websiteUsersToAdd, ...websiteUserCollection];
    }
    return websiteUserCollection;
  }

  protected convertDateFromClient(websiteUser: IWebsiteUser): IWebsiteUser {
    return Object.assign({}, websiteUser, {
      createdDate: websiteUser.createdDate?.isValid() ? websiteUser.createdDate.format(DATE_FORMAT) : undefined,
      modifiedDate: websiteUser.modifiedDate?.isValid() ? websiteUser.modifiedDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
      res.body.modifiedDate = res.body.modifiedDate ? dayjs(res.body.modifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((websiteUser: IWebsiteUser) => {
        websiteUser.createdDate = websiteUser.createdDate ? dayjs(websiteUser.createdDate) : undefined;
        websiteUser.modifiedDate = websiteUser.modifiedDate ? dayjs(websiteUser.modifiedDate) : undefined;
      });
    }
    return res;
  }
}
