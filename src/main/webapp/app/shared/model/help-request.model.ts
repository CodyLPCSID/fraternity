import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { ICategory } from 'app/shared/model/category.model';

export interface IHelpRequest {
    id?: number;
    title?: string;
    description?: string;
    datePost?: Moment;
    dateStart?: Moment;
    dateEnd?: Moment;
    user?: IUser;
    category?: ICategory;
}

export class HelpRequest implements IHelpRequest {
    constructor(
        public id?: number,
        public title?: string,
        public description?: string,
        public datePost?: Moment,
        public dateStart?: Moment,
        public dateEnd?: Moment,
        public user?: IUser,
        public category?: ICategory
    ) {}
}
