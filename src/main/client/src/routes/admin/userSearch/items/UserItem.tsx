import { useState } from "react";
import PencilLogo from "@/src/assets/icons/pencil.svg?react";
import BinLogo from "@/src/assets/icons/bin.svg?react";
import AttentionSignLogo from '@/src/assets/icons/attention-sign.svg?react';
import EditUserModal from "../modal/EditUserModal";
import { Env } from "@/src/Env";
import ConfirmDialog from "@/src/components/popup/ConfirmDialog";

// represent the item of the user data inside the userList of the admin page
export default function UserItem({id, username, email, role, refreshUserTable} : {id: number, username:string, email:string, role:string, refreshUserTable: ()=>void}) {
    const [dialogHandler, setDialogHandler] = useState<{editModal: boolean, confirmDialog: boolean}>({
        editModal: false,
        confirmDialog: false
    });
      

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
    };

    const editUser = (refresh: boolean) => {
        setDialogHandler({...dialogHandler, editModal: false});
        if (refresh) refreshUserTable();
    }

    const deleteUser = async () => {
        const response = await fetch(`${Env.API_BASE_URL}/api/users/delete/${id}`, {
            method: "DELETE",
            credentials: "include",
        });
        if (response.ok) {
            refreshUserTable();
        }
    }

    return (
        <div className="w-full flex flex-row items-center bg-violet-800 border-2 rounded-xl border-violet-500 text-white p-1">
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
                <button className="p-1 mx-1 bg-amber-400 hover:bg-amber-300 active:bg-amber-500 border-2 border-white rounded-lg text-white text-center cursor-pointer" onClick={()=>setDialogHandler({...dialogHandler, editModal: true})}><PencilLogo className="w-8 h-8 fill-white"/></button>
                {dialogHandler.editModal && <EditUserModal id={id} userInfo={{username, email, role}} open={dialogHandler.editModal} close={editUser}/>}
                <button className="p-1 mx-1 bg-red-400 hover:bg-red-500 active:bg-red-500 border-2 border-white rounded-lg text-white text-center cursor-pointer" onClick={()=>setDialogHandler({...dialogHandler, confirmDialog: true})}><BinLogo className="w-8 h-8 fill-white"/></button>
                <ConfirmDialog open={dialogHandler.confirmDialog} close={()=>setDialogHandler({...dialogHandler, confirmDialog: false})} title={`Delete ${username}?`} text={"Are you sure you want to delete this user?"} okButtonText="Remove" Icon={<AttentionSignLogo className="w-12 h-12 fill-red-600"/>} callback={deleteUser}/>
            </div>

        </div>
    );
}