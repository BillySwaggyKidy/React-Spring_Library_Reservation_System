import { bookSummaryType } from "@/src/types/book";
import { useNavigate } from "react-router-dom";

export default function BookCard({id, title, bookCoverUrl, status} : bookSummaryType) {

    const navigate = useNavigate();

    const goToDetails = () => {
        navigate("/books/" + id);
    }

    return (
        <div data-testid="bookCard" className="w-36 flex-col justify-between items-center border-2 border-gray-500 m-2 p-2 bg-gray-200 cursor-pointer transition duration-200 ease-in-out hover:scale-110" onClick={goToDetails}>
            <img src={bookCoverUrl} alt={"image " + title}/>
            <p className="text-lg font-bold text-center text-black">{title}</p>
            <div id={`${id} ${status.available}`} className="w-full h-8 flex-row justify-end items-end mr-2"><p className="text-black">{!status.available && "Reserved"}</p></div>
        </div>
    );
};