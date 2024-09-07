import { Entity, PrimaryGeneratedColumn, Column, Repository, DataSource, EntityRepository } from 'typeorm';
import { Users } from "../entities/users.entity"
import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';

@Injectable()
export class UsersRepository {

    // constructor(private repository: Repository<Users>) { }

    constructor(
        @InjectRepository(Users)
        private readonly repository: Repository<Users>,
    ) { }

    async save(users: Users): Promise<void> {
        this.repository.save(users);
    }

    async findByUserId(id: string): Promise<Users | undefined> {
        const user = await this.repository.findOne({ where: { id } });
        return user || undefined;
    }

}