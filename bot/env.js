const loadEnvVariable = (key) => {
    const value = process.env[key];

    if (!value) {
        throw new Error(`The env variable ${key} is not configured.`)
    }

    return value;
};

export const loadConfiguration = () => {
    return {
        token: loadEnvVariable("DISCORD_TOKEN"),
        url: loadEnvVariable("API_URL"),
        credentials: {
            username: loadEnvVariable("BOT_USERNAME"),
            password: loadEnvVariable("BOT_PASSWORD")
        }
    }
};


