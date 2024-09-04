
import { Entity, PrimaryColumn } from "typeorm";
import { v4 as uuidv4 } from 'uuid';
import { Domain } from "./value/domain";

@Entity()
export class BaseEntity {

    @PrimaryColumn()
    id: string

    constructor(domain: Domain) {
        this.id = domain + "_" + uuidv4().replace(/-/g, "");
    }
}