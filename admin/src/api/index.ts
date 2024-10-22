const API_URL = "https://api.worldle.vrba.dev";

export type ChallengeOption = {
    country_flag: string;
    country_name: string;
    is_correct: boolean;
};

export type Challenge = {
    answer_city_name: string;
    answer_country_flag: string;
    answer_country_name: string;
    challenge_date: string;
    image_url: string;
    options: ChallengeOption[];
};

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

export const loadChallenge = async (auth: string, date: string): Promise<Challenge> => {
    const response = await fetch(API_URL + "/api/v1/admin/challenge/date/" + date, {
        credentials: "omit",
        headers: {
            "Accept": "application/json",
            "Authorization": auth
        }
    });

    return await response.json();
};

export const regenerateChallenge = async (auth: string, date: string): Promise<Challenge> => {
    const response = await fetch(API_URL + "/api/v1/admin/challenge/date/" + date + "/regenerate", {
        method: "POST",
        credentials: "omit",
        headers: {
            "Accept": "application/json",
            "Authorization": auth
        }
    });

    return await response.json();
};
