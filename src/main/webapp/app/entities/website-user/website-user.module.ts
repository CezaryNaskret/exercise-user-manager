import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WebsiteUserComponent } from './list/website-user.component';
import { WebsiteUserDetailComponent } from './detail/website-user-detail.component';
import { WebsiteUserUpdateComponent } from './update/website-user-update.component';
import { WebsiteUserDeleteDialogComponent } from './delete/website-user-delete-dialog.component';
import { WebsiteUserRoutingModule } from './route/website-user-routing.module';

@NgModule({
  imports: [SharedModule, WebsiteUserRoutingModule],
  declarations: [WebsiteUserComponent, WebsiteUserDetailComponent, WebsiteUserUpdateComponent, WebsiteUserDeleteDialogComponent],
  entryComponents: [WebsiteUserDeleteDialogComponent],
})
export class WebsiteUserModule {}
