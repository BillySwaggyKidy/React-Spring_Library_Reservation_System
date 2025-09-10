export interface accountType {
    id: number,
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

export interface editUserInfoType {
    username: string,
    email: string,
    role: string
} 

export interface userPingResponse {
    authenticated: boolean,
    id: number,
    username: string,
    role: string
}