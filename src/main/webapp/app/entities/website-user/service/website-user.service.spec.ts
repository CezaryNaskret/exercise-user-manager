import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IWebsiteUser, WebsiteUser } from '../website-user.model';

import { WebsiteUserService } from './website-user.service';

describe('WebsiteUser Service', () => {
  let service: WebsiteUserService;
  let httpMock: HttpTestingController;
  let elemDefault: IWebsiteUser;
  let expectedResult: IWebsiteUser | IWebsiteUser[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WebsiteUserService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      login: 'AAAAAAA',
      firstName: 'AAAAAAA',
      lastName: 'AAAAAAA',
      email: 'AAAAAAA',
      language: 'AAAAAAA',
      profiles: 'AAAAAAA',
      createdDate: currentDate,
      modifiedDate: currentDate,
      activated: false,
      password: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createdDate: currentDate.format(DATE_FORMAT),
          modifiedDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a WebsiteUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createdDate: currentDate.format(DATE_FORMAT),
          modifiedDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          modifiedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new WebsiteUser()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WebsiteUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          login: 'BBBBBB',
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          email: 'BBBBBB',
          language: 'BBBBBB',
          profiles: 'BBBBBB',
          createdDate: currentDate.format(DATE_FORMAT),
          modifiedDate: currentDate.format(DATE_FORMAT),
          activated: true,
          password: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          modifiedDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WebsiteUser', () => {
      const patchObject = Object.assign(
        {
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          email: 'BBBBBB',
          createdDate: currentDate.format(DATE_FORMAT),
          activated: true,
        },
        new WebsiteUser()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createdDate: currentDate,
          modifiedDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WebsiteUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          login: 'BBBBBB',
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          email: 'BBBBBB',
          language: 'BBBBBB',
          profiles: 'BBBBBB',
          createdDate: currentDate.format(DATE_FORMAT),
          modifiedDate: currentDate.format(DATE_FORMAT),
          activated: true,
          password: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createdDate: currentDate,
          modifiedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a WebsiteUser', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWebsiteUserToCollectionIfMissing', () => {
      it('should add a WebsiteUser to an empty array', () => {
        const websiteUser: IWebsiteUser = { id: 123 };
        expectedResult = service.addWebsiteUserToCollectionIfMissing([], websiteUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(websiteUser);
      });

      it('should not add a WebsiteUser to an array that contains it', () => {
        const websiteUser: IWebsiteUser = { id: 123 };
        const websiteUserCollection: IWebsiteUser[] = [
          {
            ...websiteUser,
          },
          { id: 456 },
        ];
        expectedResult = service.addWebsiteUserToCollectionIfMissing(websiteUserCollection, websiteUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WebsiteUser to an array that doesn't contain it", () => {
        const websiteUser: IWebsiteUser = { id: 123 };
        const websiteUserCollection: IWebsiteUser[] = [{ id: 456 }];
        expectedResult = service.addWebsiteUserToCollectionIfMissing(websiteUserCollection, websiteUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(websiteUser);
      });

      it('should add only unique WebsiteUser to an array', () => {
        const websiteUserArray: IWebsiteUser[] = [{ id: 123 }, { id: 456 }, { id: 60037 }];
        const websiteUserCollection: IWebsiteUser[] = [{ id: 123 }];
        expectedResult = service.addWebsiteUserToCollectionIfMissing(websiteUserCollection, ...websiteUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const websiteUser: IWebsiteUser = { id: 123 };
        const websiteUser2: IWebsiteUser = { id: 456 };
        expectedResult = service.addWebsiteUserToCollectionIfMissing([], websiteUser, websiteUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(websiteUser);
        expect(expectedResult).toContain(websiteUser2);
      });

      it('should accept null and undefined values', () => {
        const websiteUser: IWebsiteUser = { id: 123 };
        expectedResult = service.addWebsiteUserToCollectionIfMissing([], null, websiteUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(websiteUser);
      });

      it('should return initial array if no WebsiteUser is added', () => {
        const websiteUserCollection: IWebsiteUser[] = [{ id: 123 }];
        expectedResult = service.addWebsiteUserToCollectionIfMissing(websiteUserCollection, undefined, null);
        expect(expectedResult).toEqual(websiteUserCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
