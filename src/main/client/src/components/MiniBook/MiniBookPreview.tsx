import { useNavigate } from "react-router-dom";

// show a small display of the book, used in the written by same author and reservation details page
export default function MiniBookPreview({id, imgSrc, title} : {id : number, imgSrc : string, title : string}) {
    const navigate =  useNavigate();

    const goToDetails = () => {
        navigate("/books/" + id);
    }

    return (
        <div className="w-36 flex-col justify-between items-center m-2 p-2 bg-transparent cursor-pointer transition duration-200 ease-in-out hover:scale-110" onClick={goToDetails}>
            <img className="w-full h-40" src={imgSrc} alt={"image " + title}/>
            <p className="text-sm md:text-lg font-bold text-center text-black mt-2 truncate">{title}</p>
        </div>
    );
};