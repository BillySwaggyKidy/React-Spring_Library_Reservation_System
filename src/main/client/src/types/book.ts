export interface bookStatusType {
    available: boolean,
    condition: string,
    beAvailableAt: Date
}

export interface bookSummaryType {
    id: number,
    title: string,
    bookCoverUrl: string,
    authorName: string,
    status: bookStatusType
};

export interface bookDetailsType extends bookSummaryType {
    description: string,
    genres: string[],
    authorId: number,
    authorName: string,
    publishDate: Date,
    volume: number,
    totalPages: number
};