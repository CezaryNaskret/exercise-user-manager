import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWebsiteUser, WebsiteUser } from '../website-user.model';
import { WebsiteUserService } from '../service/website-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-website-user-update',
  templateUrl: './website-user-update.component.html',
})
export class WebsiteUserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    login: [null, [Validators.required]],
    firstName: [],
    lastName: [],
    email: [],
    language: [],
    profiles: [],
    createdDate: [],
    modifiedDate: [],
    activated: [],
    password: [null, [Validators.required]],
    users: [],
  });

  constructor(
    protected websiteUserService: WebsiteUserService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ websiteUser }) => {
      this.updateForm(websiteUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const websiteUser = this.createFromForm();
    if (websiteUser.id !== undefined) {
      this.subscribeToSaveResponse(this.websiteUserService.update(websiteUser));
    } else {
      this.subscribeToSaveResponse(this.websiteUserService.create(websiteUser));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  getSelectedUser(option: IUser, selectedVals?: IUser[]): IUser {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWebsiteUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(websiteUser: IWebsiteUser): void {
    this.editForm.patchValue({
      id: websiteUser.id,
      login: websiteUser.login,
      firstName: websiteUser.firstName,
      lastName: websiteUser.lastName,
      email: websiteUser.email,
      language: websiteUser.language,
      profiles: websiteUser.profiles,
      createdDate: websiteUser.createdDate,
      modifiedDate: websiteUser.modifiedDate,
      activated: websiteUser.activated,
      password: websiteUser.password,
      users: websiteUser.users,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, ...(websiteUser.users ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, ...(this.editForm.get('users')!.value ?? []))))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IWebsiteUser {
    return {
      ...new WebsiteUser(),
      id: this.editForm.get(['id'])!.value,
      login: this.editForm.get(['login'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      language: this.editForm.get(['language'])!.value,
      profiles: this.editForm.get(['profiles'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      modifiedDate: this.editForm.get(['modifiedDate'])!.value,
      activated: this.editForm.get(['activated'])!.value,
      password: this.editForm.get(['password'])!.value,
      users: this.editForm.get(['users'])!.value,
    };
  }
}
