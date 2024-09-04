import { plainToClass } from 'class-transformer'
import { validateSync } from 'class-validator'
import { ClassConstructor } from 'class-transformer/types/interfaces'

function ValidateConfig<T extends object>(
    config: Record<string, unknown>,
    envClass: ClassConstructor<T>
) {
    const ValidateConfig = plainToClass(envClass, config, {
        enableImplicitConversion: true
    })
    const errors = validateSync(envClass, {
        skipMissingProperties: false
    })

    if (errors.length > 0) {
        throw new Error(errors.toString())
    }
    return ValidateConfig
}

export default ValidateConfig