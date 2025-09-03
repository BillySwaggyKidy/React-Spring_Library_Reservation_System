import { bookSummaryType } from "@/src/types/book";
import { useNavigate } from "react-router-dom";

export default function ReservedBookItem({id, title, bookCoverUrl, authorName, status, deleteBookReservation} : bookSummaryType & {deleteBookReservation: (id : number) => void;}) {

    const navigate = useNavigate();

    const removeItem = () => {
        deleteBookReservation(id);
    };

    const goToBookDetails = () => {
        navigate("/books/" + id);
    };

    return (
        <div className="grid grid-cols-3 grid-rows-1 w-full mb-1 bg-white/60 py-1">
            <div className="col-span-1 flex flex-row justify-start items-center pl-2">
                <img className="h-30 w-25 transition duration-200 ease-in-out hover:scale-110 cursor-pointer" src={bookCoverUrl} alt={"image " + title} onClick={goToBookDetails}/>
                <div className="h-full flex flex-col items-start ml-2">
                    <p className="text-lg font-bold text-black">{title}</p>
                    <p className="text-black">By: {authorName}</p>
                </div>
            </div>
            <div className="col-span-1 flex flex-row justify-center items-center">
                <div className={`h-6 text-center text-white px-2 rounded-xl ${status.available ? "bg-yellow-500" : "bg-gray-600"}`}>{status.available ? "available" : "reserved"}</div>
            </div>
            <div className="col-span-1 flex flex-row justify-end items-center pr-4">
                <button className="bg-red-500 hover:bg-red-600 text-white font-bold rounded-full w-8 h-8 flex flex-row items-center justify-center text-center cursor-pointer" onClick={removeItem}>X</button>
            </div>
        </div>
    );
}