import { IAssociationMember } from 'app/shared/model/association-member.model';

export interface IAssociation {
    id?: number;
    nSiret?: string;
    statut?: string;
    name?: string;
    asso?: IAssociationMember;
}

export class Association implements IAssociation {
    constructor(
        public id?: number,
        public nSiret?: string,
        public statut?: string,
        public name?: string,
        public asso?: IAssociationMember
    ) {}
}
