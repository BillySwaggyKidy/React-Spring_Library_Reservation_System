import { bookType } from "@/src/types/book";

export default function BookCard({id, title, bookCoverUrl, description, genres, author, date, volume, reserved} : bookType) {
    return (
        <div data-testid="bookCard" className="w-36 flex-col justify-between items-center border-2 border-gray-500 m-2 p-2 bg-gray-200 cursor-pointer transition duration-200 ease-in-out hover:scale-110">
            <img src={bookCoverUrl} alt={"image " + title}/>
            <p className="text-large font-bold text-center text-black">{title}</p>
            <div id={`${id} ${description} ${date} ${volume} ${reserved} ${genres}`} className="w-full h-8 flex-row justify-end items-end mr-2"><p className="text-black">{author}</p></div>
        </div>
    );
};