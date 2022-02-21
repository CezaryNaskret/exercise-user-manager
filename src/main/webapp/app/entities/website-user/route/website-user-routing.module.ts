import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WebsiteUserComponent } from '../list/website-user.component';
import { WebsiteUserDetailComponent } from '../detail/website-user-detail.component';
import { WebsiteUserUpdateComponent } from '../update/website-user-update.component';
import { WebsiteUserRoutingResolveService } from './website-user-routing-resolve.service';

const websiteUserRoute: Routes = [
  {
    path: '',
    component: WebsiteUserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WebsiteUserDetailComponent,
    resolve: {
      websiteUser: WebsiteUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WebsiteUserUpdateComponent,
    resolve: {
      websiteUser: WebsiteUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WebsiteUserUpdateComponent,
    resolve: {
      websiteUser: WebsiteUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(websiteUserRoute)],
  exports: [RouterModule],
})
export class WebsiteUserRoutingModule {}
