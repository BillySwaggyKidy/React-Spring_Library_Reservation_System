import { useState } from "react";
import EditUserModal from "../modal/EditUserModal";


export default function UserItem({id, username, email, role} : {id: number, username:string, email:string, role:string}) {
    const [displayModal, setDisplayModal] = useState<boolean>(false);

    const colorBgRole = (role:string) => {
        switch (role) {
            case "ROLE_ADMIN": {
                return "bg-violet-300";
            }
            case "ROLE_EMPLOYEE": {
                return "bg-orange-300";
            }
            case "ROLE_CUSTOMER": {
                return "bg-teal-300";
            }
        }
    }

    return (
        <div className="w-full flex flex-row items-center bg-gray-500 border-2 rounded-xl border-gray-500 text-white px-2">
            <div className="w-4/5 flex flex-row justify-start items-center gap-4">
                <div className="flex flex-row justify-around items-center">
                    <div className={`h-10 w-10 rounded-full flex flex-row justify-center items-center ${colorBgRole(role)} border-gray-500 border-2`}>
                        <p className="font-bold text-2xl">{username.charAt(0).toUpperCase()}</p>
                    </div>
                    <p className="font-bold ml-2">{username}</p>
                </div>
                <p className="text-center">{email}</p>
                <p className="text-center">{role.replace("ROLE_","")}</p>
            </div>
            <div className="w-1/5 flex flex-row justify-end items-end">
                <button className="w-1/2 h-1/2 p-2 bg-amber-400 hover:bg-amber-300 active:bg-amber-500 border-2 border-white rounded-lg text-white text-center cursor-pointer" onClick={()=>setDisplayModal(true)}>Edit</button>
                {displayModal && <EditUserModal id={id} open={displayModal} close={()=>setDisplayModal(false)}/>}
            </div>

        </div>
    );
}