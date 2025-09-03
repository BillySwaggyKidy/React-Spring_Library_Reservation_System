import PointHandLogo from '@/src/assets/icons/Pointing_hand_cursor.svg?react';
import { UserContext } from '@/src/context/userContext';
import { Env } from "@/src/Env";
import { bookDetailsType, bookSummaryType } from "@/src/types/book";
import { useContext, useEffect, useState } from "react";
import { useLoaderData, useNavigate } from "react-router-dom";

export default function BookDetails() {
    const details = useLoaderData<bookDetailsType>();
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
            // Save state, redirect, show badge, etc
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

    const MiniBookPreview = ({id, imgSrc, title} : {id : number, imgSrc : string, title : string}) => {

        const goToDetails = () => {
            navigate("/books/" + id);
        }

        return (
            <div className="w-36 flex-col justify-between items-center m-2 p-2 bg-transparent cursor-pointer transition duration-200 ease-in-out hover:scale-110" onClick={goToDetails}>
                <img src={imgSrc} alt={"image " + title}/>
                <p className="text-large font-bold text-center text-black">{title}</p>
            </div>
        );
    };

    return (
        <div className="h-full w-full flex flex-col justify-center items-center overflow-y-auto">
            <div className="h-full w-3/4 grid grid-cols-5 bg-neutral-400/80 rounded-xl m-5">
                <div className="col-span-4 h-full flex flex-col items-starts p-2">
                    <h1 className="font-bold text-6xl">{details.title}</h1>
                    <p className="text-2xl mt-4">By: <span className="text-blue-500">{details.authorName}</span></p>
                    <p className="mt-2">
                        Genres:
                        {
                            details.genres.map((genre, index) => {
                                const text : string = index != details.genres.length - 1 ? genre + ", " : genre;
                                return <span key={genre} className="font-bold"> {text}</span>;
                            })
                        }
                    </p>
                    <div className="flex flex-row justify-evenly items-center mt-2">
                        <InfoBox text="Publish Date" subText={new Date(details.publishDate).getFullYear()}/>
                        <InfoBox text="Volume" subText={details.volume}/>
                        <InfoBox text="Pages" subText={details.totalPages}/>
                    </div>
                    <p className="mt-2 text-lg">{details.description}</p>
                    <div className="w-full h-2 mx-2 my-6 bg-gradient-to-r from-transparent via-white to-transparent"/>
                    <div className="flex flex-col items-start w-full">
                        <p className="text-white text-xl">Written by the same author:</p>
                        <div className="w-full flex flex-row items-center justify-evenly overflow-x-auto mt-4">
                            {
                                booksSameAuthor.map((book)=>
                                    <MiniBookPreview key={book.title + book.id} id={book.id} imgSrc={book.bookCoverUrl} title={book.title}/>
                                )
                            }
                        </div>
                    </div>
                </div>
                <div className="col-span-1 h-full flex flex-col items-center p-2">
                    <div className="w-full h-full flex flex-col items-center justify-between bg-transparent border-2 border-white rounded-2xl pt-2">
                        <div className="w-full flex flex-col items-center justify-start">
                            <img className="h-50 w-3/4 rounded-xl shadow" src={details.bookCoverUrl} alt={details.title}/>
                            <ReservationBookSection/>
                        </div>
                        <button className="w-full h-10 bg-gray-800 font-bold text-white text-2xl rounded-b-2xl hover:bg-gray-700 transition-colors cursor-pointer" onClick={goBack}>Go back</button>
                    </div>
                </div>
            </div>
        </div>
    );
}