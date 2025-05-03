export interface bookType {
    id: string,
    title: string,
    bookCoverUrl: string,
    description: string,
    genres: string[],
    author: string,
    date: Date,
    volume: number,
    reserved: boolean
};