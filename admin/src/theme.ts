import {extendTheme, withDefaultColorScheme} from "@chakra-ui/react";

export const theme =
    extendTheme(
        {
            colors: {
                primary: "#FF2147",
                worldle: {
                    50: "#FF2147",
                    100: "#FF2147",
                    200: "#FF2147",
                    300: "#FF2147",
                    400: "#FF2147",
                    500: "#FF2147",
                    600: "#FF2147",
                    700: "#FF2147",
                    800: "#FF2147",
                    900: "#FF2147",
                }
            },
        },
        withDefaultColorScheme({
            colorScheme: "worldle",
        })
    )
;
