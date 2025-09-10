import { UserContext } from "@/src/context/userContext";
import { useContext } from "react";
import ReservedBookList from "./reservedBookList/ReservedBookList";
import { bookSummaryType } from "@/src/types/book";
import { newReservationType } from "@/src/types/reservation";
import { Env } from "@/src/Env";
import { useNavigate } from "react-router-dom";

// represent the cart page to see the books the user want to reserve
export default function BookCart() {

    const userContext = useContext(UserContext);
    const cartContent : bookSummaryType[] = userContext?.cartContent || [];
    const navigate = useNavigate();

    const removeReservedBookItem = (id : number) => {
        userContext?.setUserBookCart(cartContent.filter((book)=>book.id != id));
    };

    // used to create a endDate that is two week from the beginDate
    function getDatePlusTwoWeeks(): Date {
        const today = new Date();
        const twoWeeksInMs = 14 * 24 * 60 * 60 * 1000; // 14 days in milliseconds
        return new Date(today.getTime() + twoWeeksInMs);
    }

    const createNewBooksReservation = async () => {
        const reservationObj : newReservationType = {
            userID: userContext.currentUser.id,
            endDate: getDatePlusTwoWeeks(),
            bookIds: cartContent.map((book)=>book.id)
        };
        const response = await fetch(`${Env.API_BASE_URL}/api/reservations/add`, {
            method: "POST",
            headers: {
            "Content-Type": "application/json",
            },
            credentials: "include", // IMPORTANT for sending/receiving cookies
            body: JSON.stringify(reservationObj),
        });
        if (response.ok) { // if it work then we redirect
            userContext.setUserBookCart([]);
            navigate('/');
        }
        else {  // if the api call didn't work, it is because one of the books is no longer available
            const bookIdsTxt = await response.text();
            // we get the list of Id of the book that are not available
            const booksIdNotAvailable: number[] = JSON.parse(bookIdsTxt.slice(bookIdsTxt.indexOf("[")));
            // we update the userBookCart to change the status of the book
            userContext.setUserBookCart(cartContent.map((book)=>{
                if (booksIdNotAvailable.includes(book.id)) {
                    book.status.available = false;
                }
                return book;
            }));
        }
    };

    return (
        <div className="h-full w-full flex flex-col justify-start items-center overflow-y-auto p-2 bg-radial from-amber-300 from-40% to-orange-200">
            <div className="h-full w-3/4 grid grid-rows-9 grid-cols-1 bg-neutral-400/80 rounded-xl px-2">
                <p className="row-span-1 text-4xl font-bold text-center mt-2">Your Books &#40;{cartContent.length} items&#41;</p> 
                <div className="row-start-2 row-span-7 flex flex-col items-center">
                    <ReservedBookList reservedBooks={cartContent} deleteBookReservation={removeReservedBookItem}/>
                </div>
                <div className="row-span-2 flex flex-row justify-end items-start">
                    <button 
                    disabled={cartContent.length == 0} 
                    className={`text-xl m-4 px-4 py-2 rounded-xl font-semibold transition-colors ${cartContent.length > 0 ? 'bg-green-500 hover:bg-green-700 cursor-pointer' : 'bg-green-500/50 cursor-not-allowed'}`} 
                    onClick={createNewBooksReservation}
                    >
                        Confirm Reserve
                    </button>
                </div> 
            </div>
        </div>
    );
}