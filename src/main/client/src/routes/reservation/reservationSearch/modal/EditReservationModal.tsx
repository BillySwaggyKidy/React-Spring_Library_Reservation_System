import { Env } from "@/src/Env";
import { editReservationType } from "@/src/types/reservation";
import { useEffect, useRef, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";

interface ReservationIFormInput {
    endDate: string
}

export default function EditReservationModal({id, reservationInfo, open, close} : {id:number, reservationInfo : editReservationType, open: boolean, close: (refresh: boolean)=>void}) {
    const { register, handleSubmit } = useForm<ReservationIFormInput>({
        defaultValues: {
            endDate: reservationInfo.endDate // we can only edit the endDate of the reservation
        }
    });
    const [errorText, setErrorText] = useState<string>("");
    const ref = useRef<HTMLDialogElement>(null);

    useEffect(() => {
        if (open) {
        ref.current?.showModal();
        } else {
        ref.current?.close();
        }
    }, [open]);

    useEffect(() => {
        const handleEsc = (event: KeyboardEvent) => {
            if (event.key === "Escape") close(false);
        };
        document.addEventListener("keydown", handleEsc);
        return () => document.removeEventListener("keydown", handleEsc);
    }, []);

    const editExistingReservation: SubmitHandler<ReservationIFormInput> = async (data : ReservationIFormInput) => {
        const response = await fetch(`${Env.API_BASE_URL}/api/reservations/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({...reservationInfo, ...data}),
        });
        
        if (response.ok) {
            setErrorText("");
            close(true);
        } 
        else {
            // Show error
            setErrorText("Couldn't edit the account, please try later");
        }
    };
    

    return (
        <dialog ref={ref} className="w-3/10 h-6/10 fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 grid grid-cols-1 grid-rows-7 backdrop:bg-black/60 animate-fade-in-scale bg-amber-700 border-2 border-white rounded-2xl p-2">
            <button 
                className=" font-modal-close-icon text-gray-400 font-bold text-4xl rounded-sm absolute top-0 right-0 px-2 bg-transparent cursor-pointer font-" onClick={()=>close(false)}>×</button>
            <div className="w-full row-start-1">
                <h1 className="font-bold text-4xl text-center">Edit Reservation</h1>
            </div>
            <div className="w-full row-start-2 row-span-6 flex flex-col items-center">
                <form className="w-full flex flex-col justify-around items-center py-2" onSubmit={handleSubmit(editExistingReservation)}>
                    <div className="w-full flex flex-col items-start">
                        <label className="text-2xl">Username</label>
                        <input className="rounded-md border-2 border-white bg-green-900/60 h-8 text-xl w-3/4 mb-2" value={reservationInfo.username} disabled/>
                    </div>
                    <div className="w-full flex flex-col items-start">
                        <label className="text-2xl">Begin date</label>
                        <input className="rounded-md border-2 border-white bg-green-900/60 h-8 text-xl w-3/4 mb-2" type="date" value={reservationInfo.beginDate} disabled/>
                    </div>
                    <div className="w-full flex flex-col items-start">
                        <label className="text-2xl">End date</label>
                        <input className="rounded-md border-2 border-white bg-green-900/60 h-8 text-xl w-3/4 mb-2" type="date" {...register("endDate", { required: true })} />
                    </div>
                    
                    <input className="mt-4 w-full sm:w-3/4 py-2 px-4 bg-orange-600 border-2 border-orange-800 rounded-lg text-white text-xl font-semibold transition-colors hover:bg-orange-700 active:bg-orange-500 cursor-pointer" type="submit" value={"Edit reservation"}/>
                </form>
                <div className="h-[10%]">
                    {
                        errorText.length > 0 &&
                        <div className="p-2 border-2 border-gray-500 rounded-lg bg-red-600/70 flex flex-row justify-center items-center mt-2">
                            <p className="text-white text-center text-lg font-bold">{errorText}</p>
                        </div>
                    }
                </div>
            </div>
        </dialog>
    );
}