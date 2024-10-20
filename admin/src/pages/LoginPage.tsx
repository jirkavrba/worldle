import {FC, useState} from "react";
import {Button, Card, CardBody, Flex, Heading, Input} from "@chakra-ui/react";
import {checkAuthentication} from "../api";
import {useAuthenticationContext} from "../context/AuthenticationContext.tsx";

export const LoginPage: FC = () => {
    const auth = useAuthenticationContext();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const valid = username.trim().length > 0 && password.trim().length > 0;
    const submit = async () => {
        if (valid) {
            const header = "Basic " + btoa(username + ":" + password);
            if (await checkAuthentication(header)) {
                auth.setAuthentication(header);
            }
        }
    }

    return (
        <Flex justifyContent="center" marginTop={10}>
            <Card>
                <CardBody shadow="2xl">
                    <Heading size="md">Login</Heading>

                    <Input type="text"
                           autoComplete="off"
                           autoFocus={true}
                           marginTop={3}
                           focusBorderColor="primary"
                           value={username}
                           onChange={(event) => setUsername(event.target.value)}
                    />

                    <Input type="password"
                           autoComplete="off"
                           autoFocus={false}
                           marginTop={3}
                           focusBorderColor="primary"
                           value={password}
                           onChange={(event) => setPassword(event.target.value)}
                    />

                    <Button width="full" marginTop={3} disabled={!valid} onClick={submit}>
                        Login
                    </Button>
                </CardBody>
            </Card>
        </Flex>
    );
};
