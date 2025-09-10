import { bookSummaryType } from "@/src/types/book";
import { useNavigate } from "react-router-dom";
import BinLogo from '@/src/assets/icons/bin.svg?react';

// represent the data of the book the user reserved inside the list in the cart page
export default function ReservedBookItem({id, title, bookCoverUrl, authorName, status, deleteBookReservation} : bookSummaryType & {deleteBookReservation: (id : number) => void;}) {

    const navigate = useNavigate();

    const removeItem = () => {
        deleteBookReservation(id);
    };

    const goToBookDetails = () => {
        navigate("/books/" + id);
    };

    return (
        <div className="w-full grid grid-cols-3 mb-1 bg-white/60 py-1 hover:bg-white/80 transition-colors rounded-md">
            <div className="col-span-1 flex flex-row justify-start items-center pl-2">
                <img className="h-32 w-24 transition duration-200 ease-in-out hover:scale-110 cursor-pointer" src={bookCoverUrl} alt={"image " + title} onClick={goToBookDetails}/>
                <div className="h-full flex flex-col items-start ml-2">
                    <p className="text-lg font-bold text-black">{title}</p>
                    <p className="text-black">By: {authorName}</p>
                </div>
            </div>
            <div className="col-span-1 flex flex-row justify-center items-center">
                <div className={`h-6 text-center text-white px-2 rounded-xl ${status.available ? "bg-yellow-500" : "bg-gray-600"}`}>{status.available ? "available" : "reserved"}</div>
            </div>
            <div className="col-span-1 flex flex-row justify-end items-center pr-4">
                <button className="p-1.5 mx-1 bg-red-400 hover:bg-red-500 active:bg-red-500 border-2 border-white rounded-full text-white text-center shadow-xl cursor-pointer" onClick={removeItem}><BinLogo className="w-8 h-8 fill-white"/></button>
            </div>
        </div>
    );
}