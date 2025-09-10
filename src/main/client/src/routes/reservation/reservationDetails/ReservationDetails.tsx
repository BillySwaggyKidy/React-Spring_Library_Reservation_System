import MiniBookPreview from "@/src/components/MiniBook/MiniBookPreview";
import OkDialog from "@/src/components/popup/OkDialog";
import InfoSign from "@/src/assets/icons/info-circle.svg?react";
import { UserContext } from "@/src/context/userContext";
import { Env } from "@/src/Env";
import { reservationDetailsType } from "@/src/types/reservation";
import { dateToISOString, formatDate } from "@/src/utils/formatDate";
import { useContext, useEffect, useState } from "react";
import { useLoaderData, useNavigate } from "react-router-dom";

// represent the details of a reservation
export default function ReservationDetails() {
    const details = useLoaderData<reservationDetailsType>();
    const userContext = useContext(UserContext);
    const userData = userContext.currentUser;
    const navigate = useNavigate();
    const [okDialog, setOkDialog] = useState<boolean>(false); // use to display the ok dialog for when we retrieve the books
    const [errorText, setErrorText] = useState<string>("");

    useEffect(() => {
        if (!userData || ["ROLE_ANONYMOUS","ROLE_CUSTOMER"].includes(userData.role)) {
            navigate("/");
        }
    }, [userContext, navigate]);

    const goBack = () => {
        setOkDialog(false);
        navigate(-1);
    };

    // this function check if the reservation endDate is expired (we add 2 days for margin)
    const reservationDateIsExpired = () => {
        const todayDate = new Date();
        const reservationDate = new Date(details.endDate);
        reservationDate.setHours(reservationDate.getHours() + 48);
        return todayDate <= reservationDate;
    }

    // we tell the database that we got the books back to change their status and also the endDate to today
    const retrieveBooksFromReservation = async () => {
        const response = await fetch(`${Env.API_BASE_URL}/api/reservations/retrieve/${details.id}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include", // IMPORTANT for sending/receiving cookies
            body: JSON.stringify({
                ...details,
                endDate: dateToISOString(new Date()),
                bookIds: details.content.map((book)=>book.id)
                    
            })
        });
        
        if (response.ok) {
            setErrorText("");
            setOkDialog(true);
        } 
        else {
            // Show error
            setErrorText("Couldn't edit the account, please try later");
        }
    }

    // display the date information of the reservation
    const InfoDateBox = ({ text, subText }: { text: string; subText: string | number }) => {
        return (
            <div className="border-2 border-black rounded-xl flex flex-col items-center justify-center p-2 bg-white basis-[20%] min-w-[120px] shadow-sm">
                <p className="text-gray-600 text-sm font-medium">{text}</p>
                <div className="px-3 py-1 bg-gray-200 flex items-center justify-center rounded-xl border border-black mt-1">
                    <p className="text-black font-semibold text-center">{subText}</p>
                </div>
            </div>
        );
    };
    return (
        <div className="flex-1 w-full flex flex-col justify-start items-center overflow-y-auto relative p-4 bg-reservation bg-[length:100%_100%]">
            <div className="w-3/4 bg-stone-600/90 rounded-xl p-6 text-white flex flex-col gap-4 relative">
                <button className="absolute top-4 right-4 p-2 border-2 border-gray-400 bg-white rounded-md font-semibold text-black hover:bg-gray-100 transition cursor-pointer" onClick={goBack}>Go back</button>
                <div className="w-full h-full flex flex-col items-starts p-2">
                    <h1 className="font-bold text-4xl md:text-5xl lg:text-6xl text-center">Reservation n°{details.id}</h1>
                    <p className="text-2xl mt-4">By user n°{details.userID}: <span className="text-blue-500">{details.username}</span> </p>
                    <div className="flex flex-col sm:flex-row justify-around items-center mt-4 gap-4">
                        <InfoDateBox text="Begin date" subText={formatDate(details.beginDate)}/>
                        <InfoDateBox text="End date" subText={formatDate(details.endDate)}/>
                    </div>
                    <div className="flex flex-col items-start w-full">
                        <div className="w-full h-2 my-1.5 bg-gradient-to-r from-transparent via-white to-transparent rounded-full"/>
                        <p className="w-full text-white text-3xl text-right">&#40;{details.content.length} items&#41;</p>
                        <div className="w-full flex gap-4 overflow-x-auto mt-4 px-2">
                            {
                                details.content.map((book)=>
                                    <MiniBookPreview key={book.title + book.id} id={book.id} imgSrc={book.bookCoverUrl} title={book.title}/>
                                )
                            }
                        </div>
                    </div>
                    <div className="flex flex-row justify-end items-center">
                        {
                            errorText.length > 0 && 
                            <div className="bg-slate-500 border-2 p-2 rounded-xl mr-2">
                                <p className="font-bold text-center text-red-500">{errorText}</p>
                            </div>
                        }
                        {reservationDateIsExpired() && <button className=" bg-yellow-500 border-2 hover:bg-yellow-400 border-amber-500 rounded-md font-semibold text-amber-900 text-xl transition cursor-pointer p-1" onClick={retrieveBooksFromReservation}>Retrieve Books</button>}
                    </div>
                    {okDialog && <OkDialog open={okDialog} close={()=>setOkDialog(false)} title={"The reservation books are retrieved"} text={"The books are now available again"} okButtonText="Ok" Icon={<InfoSign className="w-12 h-12 fill-blue-500"/>} callback={goBack}/>}
                </div>
            </div>
        </div>
    );
}