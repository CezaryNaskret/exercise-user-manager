import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IWebsiteUser {
  id?: number;
  login?: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  language?: string | null;
  profiles?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  activated?: boolean | null;
  password?: string;
  users?: IUser[] | null;
}

export class WebsiteUser implements IWebsiteUser {
  constructor(
    public id?: number,
    public login?: string,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public language?: string | null,
    public profiles?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public modifiedDate?: dayjs.Dayjs | null,
    public activated?: boolean | null,
    public password?: string,
    public users?: IUser[] | null
  ) {
    this.activated = this.activated ?? false;
  }
}

export function getWebsiteUserIdentifier(websiteUser: IWebsiteUser): number | undefined {
  return websiteUser.id;
}
