import PointHandLogo from '@/src/assets/icons/Pointing_hand_cursor.svg?react';
import MiniBookPreview from '@/src/components/MiniBook/MiniBookPreview';
import { UserContext } from '@/src/context/userContext';
import { Env } from "@/src/Env";
import { bookDetailsType, bookSummaryType } from "@/src/types/book";
import { useContext, useEffect, useState } from "react";
import { useLoaderData, useNavigate } from "react-router-dom";

// this page represent the details of a book in the search book page
export default function BookDetails() {
    const details = useLoaderData<bookDetailsType>();
    // represent the list of book written by the same author
    const [booksSameAuthor, setBooksSameAuthor] = useState<bookSummaryType[]>([]);
    const userContext = useContext(UserContext);
    const userData = userContext.currentUser;
    const navigate = useNavigate();

    const goBack = () => {
        navigate(-1);
    };

    const setBookOnReserveCart = () => {
        const cartContent = userContext?.cartContent;
        if (!cartContent) return; // bail early if undefined

        // we check if the book is not already in the cart before putting it in
        const exists = cartContent.some((book) => book.id === details.id);

        if (!exists) {
            const newSummaryBook: bookSummaryType = {
                id: details.id,
                title: details.title,
                bookCoverUrl: details.bookCoverUrl,
                authorName: details.authorName,
                status: details.status,
            };
            userContext.setUserBookCart([...cartContent, newSummaryBook]);
        }
    };

    const getBookWrittenBySameAuthor = async () => {
        const response = await fetch(`${Env.API_BASE_URL}/api/books/author/${details.authorName}`, {
            method: "GET",
            credentials: "include",
        });
        if (response.ok) {
            const booksResponse : bookSummaryType[] = await response.json();
            setBooksSameAuthor(booksResponse.filter((book)=>book.id != details.id));
        }
    };

    useEffect(()=>{
        getBookWrittenBySameAuthor();
    },[details]);

    const InfoBox = ({text, subText} : {text:string, subText:string | number}) => {

        return (
            <div className="border-2 border-black rounded-xl flex flex-col justify-start items-center p-1 bg-white basis-30">
                <p className="text-gray-500">{text}</p>
                <p className="text-black text-lg">{subText}</p>
            </div>
        );
    };

    const ReservationBookSection = () => {        
        if (userData.role != "ROLE_ANONYMOUS") {
            if (details.status.available) {
                return (
                    <button className="w-8/10 bg-yellow-600 font-bold text-white text-2xl my-4 rounded-lg transition-colors hover:bg-yellow-400 cursor-pointer flex flex-row justify-center items-center" onClick={setBookOnReserveCart}>
                        <PointHandLogo className="w-8 h-8 fill-white"/> 
                        Book 
                        <PointHandLogo className="w-8 h-8 fill-white"/>
                    </button>
                );
            }
            else {
                return (
                    <span className="text-red-600 shadow-amber-50 text-lg text-center">Book is not available until: <br/> {new Date(details.status.beAvailableAt).toDateString()}</span>
                );
            }
        }
    }

    return (
        <div className="h-full w-full flex flex-col justify-center items-center overflow-y-auto py-2">
            <div className="h-full w-3/4 grid grid-cols-5 bg-white/90 shadow-lg rounded-xl p-6 gap-6">
                <div className="col-span-4 h-full flex flex-col items-starts p-2 overflow-y-auto">
                    <h1 className="font-bold text-black text-4xl md:text-5xl lg:text-6xl break-words whitespace-normal">{details.title}</h1>
                    <p className="text-lg md:text-2xl mt-4 text-gray-700">By: <span className="text-blue-600 font-semibold">{details.authorName}</span></p>
                    <p className="mt-3 flex flex-wrap gap-2">
                        {details.genres.map((genre) => (
                            <span key={genre} className="bg-blue-100 text-blue-700 text-sm font-medium px-2 py-0.5 rounded-full">
                                {genre}
                            </span>
                        ))}
                    </p>
                    <div className="flex flex-row flex-wrap gap-6 mt-4">
                        <InfoBox text="Publish Date" subText={new Date(details.publishDate).getFullYear()}/>
                        {details.volume && <InfoBox text="Volume" subText={details.volume}/>}
                        <InfoBox text="Pages" subText={details.totalPages}/>
                    </div>
                    <p className="mt-4 text-base md:text-lg leading-relaxed text-gray-800">{details.description}</p>
                    {
                        booksSameAuthor.length > 0 &&
                        <>
                            <div className="w-full h-2 my-6 border-3 border-gray-200"></div>
                            <div className="flex flex-col items-start w-full">
                                <p className="text-xl font-semibold text-gray-800 border-b-2 border-amber-600 inline-block pb-1">More from {details.authorName}:</p>
                                <div className="w-full flex flex-row items-start justify-evenly overflow-x-auto mt-4">
                                    {
                                        booksSameAuthor.map((book)=>
                                            <MiniBookPreview key={book.title + book.id} id={book.id} imgSrc={book.bookCoverUrl} title={book.title}/>
                                        )
                                    }
                                </div>
                            </div>
                        </>
                    }
                </div>
                <div className="col-span-1 h-full flex flex-col items-center p-2">
                    <div className="w-full h-full flex flex-col items-center justify-between bg-gray-50 rounded-xl shadow-md p-4">
                        <div className="w-full flex flex-col items-center justify-start">
                            <img className="h-60 rounded-xl shadow" src={details.bookCoverUrl} alt={details.title}/>
                            <ReservationBookSection/>
                        </div>
                        <button className="w-full h-10 bg-amber-700 text-white font-semibold text-lg rounded-md hover:bg-amber-600 transition" onClick={goBack}>Go back</button>
                    </div>
                </div>
            </div>
        </div>
    );
}