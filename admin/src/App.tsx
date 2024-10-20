import {Flex, Heading} from "@chakra-ui/react";
import {LoginPage} from "./pages/LoginPage.tsx";
import {useLocalStorage} from "@uidotdev/usehooks";
import {AuthenticationContext} from "./context/AuthenticationContext.tsx";
import {AdminPage} from "./pages/AdminPage.tsx";

function App() {
    const [authentication, setAuthentication] = useLocalStorage<string | null>("authentication", null);

    return (
        <AuthenticationContext.Provider value={{authentication, setAuthentication}}>
            <Flex direction="column" alignItems="stretch" justifyContent="start">
                <Flex direction="row" alignItems="center" justifyContent="center" padding="10">
                    <Heading>Worldle Admin UI</Heading>
                </Flex>
                {
                    authentication === null
                        ? <LoginPage/>
                        : <AdminPage/>
                }
            </Flex>
        </AuthenticationContext.Provider>
    )
}

export default App
