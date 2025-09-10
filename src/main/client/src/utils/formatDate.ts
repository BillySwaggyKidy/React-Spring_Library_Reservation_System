export const dateToISOString = (date: Date): string => {
    const newDate = new Date(date);
    return newDate.toISOString().split("T")[0];
};

export const formatDate = (date: Date): string => {
    const newDate = new Date(date);
    const day = newDate.getDate();
    const month = newDate.toLocaleString("en-US", { month: "short" }); // e.g. "Sep"
    const year = newDate.getFullYear();
    return `${day} ${month} ${year}`;
};