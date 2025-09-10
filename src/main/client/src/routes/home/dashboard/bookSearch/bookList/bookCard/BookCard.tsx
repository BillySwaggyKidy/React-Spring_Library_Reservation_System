import { bookSummaryType } from "@/src/types/book";
import { useNavigate } from "react-router-dom";

// represent the display of the book with the image and the title inside the book search section in the home page
export default function BookCard({id, title, bookCoverUrl, status} : bookSummaryType) {

    const navigate = useNavigate();

    const goToDetails = () => {
        navigate("/books/" + id);
    }

    return (
        <div data-testid="bookCard" className="relative w-40 flex flex-col items-center rounded-lg shadow-md bg-white m-3 p-3 transition transform duration-200 hover:scale-105 hover:shadow-lg cursor-pointer" onClick={goToDetails}>
            <img className="w-full h-52 object-cover rounded-md" src={bookCoverUrl} alt={"image " + title}/>
            <p className="mt-2 text-sm font-semibold text-center text-gray-800 line-clamp-2">{title}</p>
            { !status.available && (
                <span className="absolute top-2 right-2 bg-red-600 text-white text-xs font-bold px-2 py-0.5 rounded-full shadow-md">Reserved</span>
            )}
        </div>
    );
};