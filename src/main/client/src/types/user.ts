export interface accountType {
    username: string,
    role: string
};

export interface userDataType {
    id: number,
    username: string,
    email: string,
    password?: string,
    role: string
};