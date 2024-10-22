import {FC, useEffect, useState} from "react";
import {Button, Card, CardBody, Heading, Image} from "@chakra-ui/react";
import {Challenge, loadChallenge, regenerateChallenge} from "../api";
import {useAuthenticationContext} from "../context/AuthenticationContext.tsx";
import {formatDate} from "../helpers/date.ts";

export type ChallengePreviewProps = {
    date: string;
};

export const ChallengePreview: FC<ChallengePreviewProps> = ({date}) => {
    const {authentication} = useAuthenticationContext();
    const [loading, setLoading] = useState<boolean>(true);
    const [challenge, setChallenge] = useState<Challenge | null>(null);

    useEffect(() => {
        setLoading(true);
        loadChallenge(authentication!, date)
            .then(challenge => {
                setChallenge(challenge);
                setLoading(false);
            });
    }, [authentication, date]);

    const handleRegenerate = () => {
        setLoading(true);
        regenerateChallenge(authentication!, date)
            .then(result => {
                setChallenge(result);
                setLoading(false);
            });
    };

    return (
        <Card shadow="2xl" margin={5}>
            <CardBody>
                {
                    (loading || challenge === null)
                        ? (<Heading size="sm" color="primary" marginY={10} textAlign="center">Loading...</Heading>)
                        : (
                            <>
                                <Heading size="md">
                                    {challenge.answer_country_flag}
                                    {challenge.answer_country_name}
                                </Heading>
                                <Image src={challenge.image_url} borderRadius="xl"/>

                                <Button marginTop={5} disabled={date === formatDate(new Date())} onClick={() => handleRegenerate()}>Regenerate challenge</Button>
                            </>
                        )
                }
            </CardBody>
        </Card>
    );
};
