import { Entity, PrimaryGeneratedColumn, Column, Repository, DataSource } from 'typeorm';
import { Users } from "../domain/users.entity"

export class UsersRepository {
    private usersRepository: Repository<Users>;

    constructor(private readonly dataSource: DataSource) {
        this.usersRepository = this.dataSource.getRepository(Users);
    }

}