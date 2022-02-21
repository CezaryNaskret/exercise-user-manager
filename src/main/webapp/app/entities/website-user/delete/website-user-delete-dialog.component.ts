import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWebsiteUser } from '../website-user.model';
import { WebsiteUserService } from '../service/website-user.service';

@Component({
  templateUrl: './website-user-delete-dialog.component.html',
})
export class WebsiteUserDeleteDialogComponent {
  websiteUser?: IWebsiteUser;

  constructor(protected websiteUserService: WebsiteUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.websiteUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
