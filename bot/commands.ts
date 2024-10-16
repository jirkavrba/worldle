import {Client, Routes} from "npm:discord.js";

export const registerApplicationCommands = async (client: Client<true>) => {
    console.log("Registering application commands");

    const rest = client.rest;

    await rest.put(
        Routes.applicationCommands(client.application.id),
        {
            body: [
                {
                    name: "subscribe",
                    description: "Subscribes the channel to daily challenges.",
                },
                {
                    name: "unsubscribe",
                    description:
                        "Unsubscribes the channel from daily challenges.",
                },
            ],
        },
    );
};
