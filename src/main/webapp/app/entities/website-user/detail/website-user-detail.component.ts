import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWebsiteUser } from '../website-user.model';

@Component({
  selector: 'jhi-website-user-detail',
  templateUrl: './website-user-detail.component.html',
})
export class WebsiteUserDetailComponent implements OnInit {
  websiteUser: IWebsiteUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ websiteUser }) => {
      this.websiteUser = websiteUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
