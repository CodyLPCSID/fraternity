import { IHelpOffer } from 'app/shared/model/help-offer.model';
import { IHelpRequest } from 'app/shared/model/help-request.model';

export interface IHelpAction {
    id?: number;
    message?: string;
    helpOffer?: IHelpOffer;
    helpRequest?: IHelpRequest;
}

export class HelpAction implements IHelpAction {
    constructor(public id?: number, public message?: string, public helpOffer?: IHelpOffer, public helpRequest?: IHelpRequest) {}
}
