import {Client, GatewayIntentBits} from "discord.js";
import {loadConfiguration} from "./env.js";

const config = loadConfiguration();
const client = new Client({
    intents: [GatewayIntentBits.Guilds]
});

client.on("ready", () => {
    console.log("Logged in.")
});

await client.login(config.token);
