export interface IHelpAction {
    id?: number;
}

export class HelpAction implements IHelpAction {
    constructor(public id?: number) {}
}
