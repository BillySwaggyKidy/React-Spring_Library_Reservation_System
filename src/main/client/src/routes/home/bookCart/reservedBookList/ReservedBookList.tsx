import { bookSummaryType } from "@/src/types/book";
import ReservedBookItem from "./reservedBookItem/ReservedBookItem";

export default function ReservedBookList({reservedBooks, deleteBookReservation} : {reservedBooks : bookSummaryType[], deleteBookReservation: (id : number) => void;}) {
    return (
        <div className="flex flex-col items-center w-full h-full">
            <div className="flex w-full justify-between px-2 py-1 border-b-2 border-b-slate-200 mb-1">
                <p className="font-bold text-xl">Items</p>
                <p className="font-bold text-xl text-center">Available</p>
                <p className="font-bold text-xl text-right">Action</p>
            </div>
            <div className="flex flex-col items-center flex-1 min-h-0 w-full overflow-y-auto">
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