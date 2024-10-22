import {FC, useMemo} from "react";
import {addDays, formatDate} from "../helpers/date.ts";
import {ChallengePreview} from "../components/ChallengePreview.tsx";
import {Grid} from "@chakra-ui/react";

const previewDays = 9;

export const AdminPage: FC = () => {
    const dates = useMemo(() => [...new Array(previewDays)]
        .map((_, offset) => addDays(new Date(), offset))
        .map(date => formatDate(date)), []);

    return (
        <Grid templateColumns="repeat(3, 1fr)" gap={5}>
            {dates.map(date =>
                <ChallengePreview key={date} date={date}/>
            )}
        </Grid>
    );
};
