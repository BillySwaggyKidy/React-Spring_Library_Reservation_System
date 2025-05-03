import { userDataType } from "@/src/types/user";
import { useEffect, useRef } from "react";


export default function EditUserModal({id, userInfo, open, close} : {id:number, userInfo? : userDataType, open: boolean, close: ()=>void}) {

    const ref = useRef<HTMLDialogElement>(null);

    useEffect(() => {
        if (open) {
        ref.current?.showModal();
        } else {
        ref.current?.close();
        }
    }, [open]);

    const dummieFunction = () => {
        console.log(id);
        console.log(userInfo);
    }

    return (
        <dialog ref={ref} className="w-4/10 h-8/10 fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 grid grid-cols-1 grid-rows-7 backdrop:bg-black/50 animate-fade-in-scale bg-amber-700 border-2 border-white rounded-2xl p-2">
            <button 
                className=" font-modal-close-icon text-gray-400 font-bold text-4xl rounded-sm absolute top-0 right-0 px-2 bg-transparent cursor-pointer font-" onClick={close}>×</button>
            <div className="w-full row-start-1">
                <h1 className="font-bold text-4xl text-center" onChange={dummieFunction}>Edit User</h1>
            </div>
            <div className="w-full row-start-2 row-span-6 flex flex-col items-center">

            </div>
        </dialog>
    );
}