import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { ICategory } from 'app/shared/model/category.model';

export interface IHelpOffer {
    id?: number;
    title?: string;
    description?: string;
    datePost?: Moment;
    dateStart?: Moment;
    dateEnd?: Moment;
    pictureContentType?: string;
    picture?: any;
    user?: IUser;
    category?: ICategory;
}

export class HelpOffer implements IHelpOffer {
    constructor(
        public id?: number,
        public title?: string,
        public description?: string,
        public datePost?: Moment,
        public dateStart?: Moment,
        public dateEnd?: Moment,
        public pictureContentType?: string,
        public picture?: any,
        public user?: IUser,
        public category?: ICategory
    ) {}
}
