export interface reservationType {
    id: number,
    userID: number,
    beginDate: Date,
    endDate: Date,
    bookIds: number[]
}

export interface newReservationType {
    userID: number,
    endDate: Date,
    bookIds: number[]
}