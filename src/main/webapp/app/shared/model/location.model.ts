import { IHelpOffer } from 'app/shared/model/help-offer.model';

export interface ILocation {
    id?: number;
    country?: string;
    city?: string;
    street?: string;
    nbStreet?: string;
    zipCode?: string;
    longitute?: string;
    latitude?: string;
    helpOffer?: IHelpOffer;
}

export class Location implements ILocation {
    constructor(
        public id?: number,
        public country?: string,
        public city?: string,
        public street?: string,
        public nbStreet?: string,
        public zipCode?: string,
        public longitute?: string,
        public latitude?: string,
        public helpOffer?: IHelpOffer
    ) {}
}
