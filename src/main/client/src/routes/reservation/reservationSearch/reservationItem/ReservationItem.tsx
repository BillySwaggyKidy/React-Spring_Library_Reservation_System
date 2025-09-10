import { useNavigate } from "react-router-dom";
import ViewLogo from "@/src/assets/icons/view.svg?react";
import PencilLogo from "@/src/assets/icons/pencil.svg?react";
import BinLogo from "@/src/assets/icons/bin.svg?react";
import AttentionSignLogo from '@/src/assets/icons/attention-sign.svg?react';
import { useState } from "react";
import EditReservationModal from "../modal/EditReservationModal";
import ConfirmDialog from "@/src/components/popup/ConfirmDialog";
import { Env } from "@/src/Env";
import { formatDate, dateToISOString } from "@/src/utils/formatDate";

// represent the data of a reservation item in the reservation list inside the reservation page
export default function ReservationItem({id, userID, username, beginDate, endDate, refreshReservationTable} : {id: number, userID: number, username: string, beginDate : Date, endDate : Date, refreshReservationTable: ()=>void}) {
    const [dialogHandler, setDialogHandler] = useState<{editModal: boolean, confirmDialog: boolean}>({
        editModal: false,
        confirmDialog: false
    });
    const navigate = useNavigate();

    const goToReservationDetails = () => {
        navigate("/reservations/" + id);
    }

    // used by the confirm dialog, if needed it can refresh the reservation data table
    const editReservation = (refresh: boolean) => {
        setDialogHandler({...dialogHandler, editModal: false});
        if (refresh) refreshReservationTable();
    }
    
    const deleteReservation = async () => {
        const response = await fetch(`${Env.API_BASE_URL}/api/reservations/remove/${id}`, {
            method: "DELETE",
            credentials: "include", // IMPORTANT for sending/receiving cookies
        });
        if (response.ok) {
            refreshReservationTable();
        }
    }

    return (
        <div key={username + userID} className="w-full flex flex-row items-center bg-amber-700 border-2 rounded-xl border-amber-900 hover:bg-amber-800 transition-colors text-white p-1">
            <div className="w-4/5 flex flex-row justify-start items-center gap-4">
                <div className="flex flex-row justify-between items-center w-full">
                    <div className="rounded-full flex flex-row justify-center items-center bg-neutral-700 border-neutral-900 border-2 px-2">
                        <p className="font-bold text-2xl">{username}</p>
                    </div>
                </div>
                <p className="text-center">{formatDate(beginDate)}</p>
                <p className="text-center">{formatDate(endDate)}</p>
            </div>

            <div className="w-1/5 flex flex-row justify-end items-end">
                <button className="p-1 mx-1 bg-blue-400 hover:bg-blue-300 active:bg-blue-500 border-2 border-white rounded-lg text-white text-center cursor-pointer" onClick={goToReservationDetails}><ViewLogo className="w-8 h-8 fill-white"/></button>
                <button className="p-1 mx-1 bg-amber-400 hover:bg-amber-300 active:bg-amber-500 border-2 border-white rounded-lg text-white text-center cursor-pointer" onClick={()=>setDialogHandler({...dialogHandler, editModal: true})}><PencilLogo className="w-8 h-8 fill-white"/></button>
                {dialogHandler.editModal && <EditReservationModal id={id} reservationInfo={{username, userID, beginDate: dateToISOString(beginDate), endDate: dateToISOString(endDate)}} open={dialogHandler.editModal} close={editReservation}/>}
                <button className="p-1 mx-1 bg-red-400 hover:bg-red-500 active:bg-red-500 border-2 border-white rounded-lg text-white text-center cursor-pointer" onClick={()=>setDialogHandler({...dialogHandler, confirmDialog: true})}><BinLogo className="w-8 h-8 fill-white"/></button>
                <ConfirmDialog open={dialogHandler.confirmDialog} close={()=>setDialogHandler({...dialogHandler, confirmDialog: false})} title={`Delete this Reservation?`} text={"Are you sure you want to delete this reservation?"} okButtonText="Remove" Icon={<AttentionSignLogo className="w-12 h-12 fill-red-600"/>} callback={deleteReservation}/>
            </div>
        </div>
    );
}