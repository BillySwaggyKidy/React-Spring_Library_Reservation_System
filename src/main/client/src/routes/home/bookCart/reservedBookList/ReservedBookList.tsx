import { bookSummaryType } from "@/src/types/book";
import ReservedBookItem from "./reservedBookItem/ReservedBookItem";

export default function ReservedBookList({reservedBooks, deleteBookReservation} : {reservedBooks : bookSummaryType[], deleteBookReservation: (id : number) => void;}) {
    return (
        <div className="flex flex-col items-center w-full h-full">
            <div className="w-full grid grid-cols-3 grid-rows-1 justify-center border border-transparent border-b-black mb-1 px-2">
                <p className="text-xl text-black font-bold">Items</p>
                <p className="text-xl text-black font-bold text-center">Available</p>
                <p className="text-xl text-black font-bold text-right">Action</p>
            </div>
            <div className="flex flex-col items-center overflow-y-auto max-h-4/5">
                {
                    reservedBooks.length > 0 ?
                    reservedBooks.map((book) => 
                        <ReservedBookItem key={book.bookCoverUrl} {...book} deleteBookReservation={deleteBookReservation}/>
                    ) :
                    <p className="text-black text-3xl">You don't have books in your cart</p>
                }
            </div>
        </div>
    );
}