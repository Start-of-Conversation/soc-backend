export function Builder<T extends { new(...args: any[]): {} }>(constructor: T) {
    return class extends constructor {
        static builder() {
            const instance = new constructor();
            const builder: any = {};

            // Create a builder method for each property
            Object.getOwnPropertyNames(instance).forEach((key) => {
                if (key.startsWith('_')) {
                    builder[key.substring(1)] = (value: any) => {
                        instance[key] = value;
                        return builder;
                    };
                } else {
                    builder[key] = (value: any) => {
                        instance[key] = value;
                        return builder;
                    };
                }
            });

            builder.build = () => instance;

            return builder;
        }
    };
}