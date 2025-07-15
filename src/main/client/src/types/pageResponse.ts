export interface PagedResponse<T> {
    content: T[],
    page: number,
    sizePerPage: number,
    totalElements: number,
    totalPages: number
    
}