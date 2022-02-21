import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IWebsiteUser, WebsiteUser } from '../website-user.model';
import { WebsiteUserService } from '../service/website-user.service';

import { WebsiteUserRoutingResolveService } from './website-user-routing-resolve.service';

describe('WebsiteUser routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: WebsiteUserRoutingResolveService;
  let service: WebsiteUserService;
  let resultWebsiteUser: IWebsiteUser | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(WebsiteUserRoutingResolveService);
    service = TestBed.inject(WebsiteUserService);
    resultWebsiteUser = undefined;
  });

  describe('resolve', () => {
    it('should return IWebsiteUser returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWebsiteUser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWebsiteUser).toEqual({ id: 123 });
    });

    it('should return new IWebsiteUser if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWebsiteUser = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWebsiteUser).toEqual(new WebsiteUser());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as WebsiteUser })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWebsiteUser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWebsiteUser).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
