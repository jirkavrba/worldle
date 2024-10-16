import {loadConfiguration} from "./env.ts";
import {Client, GatewayIntentBits} from "npm:discord.js";
import {registerApplicationCommands} from "./commands.ts";

if (import.meta.main) {
  const config = loadConfiguration();
  const client = new Client({
    intents: [GatewayIntentBits.Guilds],
  });

  client.on("ready", (client) => registerApplicationCommands(client));
  // client.on("interactionCreate", (interaction) => )

  await client.login(config.token);
}
