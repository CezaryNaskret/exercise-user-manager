import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWebsiteUser } from '../website-user.model';

import { ASC, DESC, SORT } from 'app/config/pagination.constants';
import { WebsiteUserService } from '../service/website-user.service';
import { WebsiteUserDeleteDialogComponent } from '../delete/website-user-delete-dialog.component';

@Component({
  selector: 'jhi-website-user',
  templateUrl: './website-user.component.html',
})
export class WebsiteUserComponent implements OnInit {
  websiteUsers?: IWebsiteUser[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = 5;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  searchFieldOptions = [
    { value: 'firstName', label: 'First name' },
    { value: 'lastName', label: 'Last name' },
    { value: 'login', label: 'Login' },
    { value: 'email', label: 'Email' },
  ];

  criteria = {
    searchData: '',
    searchField: 'firstName',
  };

  activeSearchData?: string = '';
  activeSearchField?: string = 'firstName';

  constructor(
    protected websiteUserService: WebsiteUserService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    const req = this.constructRequest(pageToLoad);

    this.websiteUserService
      .query(req)
      .subscribe({
        next: (res: HttpResponse<IWebsiteUser[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IWebsiteUser): number {
    return item.id!;
  }

  delete(websiteUser: IWebsiteUser): void {
    const modalRef = this.modalService.open(WebsiteUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.websiteUser = websiteUser;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  searchWebsiteUser(): void {
    this.activeSearchData = this.criteria.searchData;
    this.activeSearchField = this.criteria.searchField;
    this.page = 1;
    this.ngbPaginationPage = 1;
    this.loadPage();
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IWebsiteUser[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/website-user'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.websiteUsers = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  protected constructRequest(pageToLoad: number): any {
    const req = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
      'firstName.contains': '',
      'lastName.contains': '',
      'login.contains': '',
      'email.contains': '',
    };
    if (this.activeSearchData) {
      if(this.activeSearchField === 'firstName'){
        req['firstName.contains'] = this.activeSearchData;
      }
      if(this.activeSearchField === 'lastName'){
        req['lastName.contains'] = this.activeSearchData;
      }
      if(this.activeSearchField === 'login'){
        req['login.contains'] = this.activeSearchData;
      }
      if(this.activeSearchField === 'email'){
        req['email.contains'] = this.activeSearchData;
      }
    }
    return req;
  }
}
