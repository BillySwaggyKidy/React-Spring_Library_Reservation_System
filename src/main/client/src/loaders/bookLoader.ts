import { LoaderFunctionArgs } from "react-router-dom";
import { Env } from "../Env";
import { bookDetailsType } from "../types/book";

// load the detail of a book
export const bookLoader = async ({ params } : LoaderFunctionArgs) => {

    const res = await fetch(`${Env.API_BASE_URL}/api/books/${params.bookId}`, {
        method: "GET",
        credentials: "include",
    });
    const bookDetails : bookDetailsType = await res.json();

    return bookDetails;
};