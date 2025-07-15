import { bookType } from "@/src/types/book";
import BookCard from "./bookCard/BookCard";

export default function BookList({booksList} : {booksList : bookType[]}) {
    return (
        
        booksList.length > 0 ? booksList.map(bookData => 
            <BookCard key={bookData.id} {...bookData} /> 
        ) : <p className="text-2xl text-center text-black font-bold">Didn't find any matching elements</p>
    )
}