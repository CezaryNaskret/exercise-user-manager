import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'website-user',
        data: { pageTitle: 'exerciseV2App.websiteUser.home.title' },
        loadChildren: () => import('./website-user/website-user.module').then(m => m.WebsiteUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
