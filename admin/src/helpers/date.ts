export const formatDate = (date: Date): string => {
    return date.toISOString().substring(0, 10);
};

export const addDays = (date: Date, days: number) => {
    const copy = new Date(date.valueOf());
    copy.setDate(date.getDate() + days);
    return copy;
};
