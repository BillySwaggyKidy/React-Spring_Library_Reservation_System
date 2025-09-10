import { bookSummaryType } from "./book"

export interface reservationItemType {
    id: number,
    userID: number,
    username: string,
    beginDate: Date,
    endDate: Date,
}

export interface reservationDetailsType extends reservationItemType {
    content: bookSummaryType[]
}

export interface editReservationType {
    userID: number,
    username: string,
    beginDate: string,
    endDate: string
}

export interface newReservationType {
    userID: number,
    endDate: Date,
    bookIds: number[]
}