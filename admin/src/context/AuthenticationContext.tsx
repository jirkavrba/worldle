import {createContext, useContext} from "react";

export type AuthenticationContextState = {
    authentication: string | null;
    setAuthentication: (header: string | null) => void;
};

export const AuthenticationContext = createContext<AuthenticationContextState | null>(null);

export const useAuthenticationContext = (): AuthenticationContextState => {
    const state = useContext(AuthenticationContext);

    if (state == null) {
        throw new Error("Authentication context is not set.");
    }

    return state;
};
