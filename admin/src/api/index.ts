const API_URL = "https://api.worldle.vrba.dev";

export const checkAuthentication = async (auth: string): Promise<boolean> => {
    const response = await fetch(API_URL + "/api/v1/admin/auth/check", {
        method: "POST",
        credentials: "omit",
        headers: {
            "Accept": "application/json",
            "Authorization": auth
        }
    });

    return response.ok;
};
