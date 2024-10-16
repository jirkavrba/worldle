export type BotConfiguration = {
  token: string;
};

const loadEnvVariable = (name: string): string => {
  const value = Deno.env.get(name);

  if (!value) {
    throw new Error(`The ${name} env variable is not configured!`);
  }

  return value;
};

export const loadConfiguration = (): BotConfiguration => {
  return {
    token: loadEnvVariable("DISCORD_TOKEN"),
  };
};
