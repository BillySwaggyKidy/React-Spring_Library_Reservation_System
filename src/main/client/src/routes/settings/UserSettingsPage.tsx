import { UserContext } from "@/src/context/userContext";
import { Env } from "@/src/Env";
import { userDataType } from "@/src/types/user";
import { useContext, useEffect, useState } from "react";
import { Outlet, useLoaderData, useNavigate } from "react-router-dom";
import AttentionSignLogo from "@/src/assets/icons/attention-sign.svg?react";
import ConfirmDialog from "@/src/components/popup/ConfirmDialog";

// represent the user settings page to either edit or delete the account informations
export default function UserSettingsPage() {
    const userContext = useContext(UserContext);
    const navigate = useNavigate();
    const [account, setAccount] = useState<userDataType>(useLoaderData<userDataType>());
    const [dialogHandler, setDialogHandler] = useState<{confirmDialog: boolean}>({
        confirmDialog: false
    });

    useEffect(() => {
        if (!userContext || userContext.currentUser?.role == "ROLE_ANONYMOUS") {
            navigate("/"); // Redirect if not an admin
        }
    }, [userContext, navigate]);

    const goToNestedRoute = (path: string) => {
        navigate(path);
    };

    // 
    const refreshAccountData = async () => {
        const response = await fetch(`${Env.API_BASE_URL}/api/users/details/${account.id}`, {
            method: "GET",
            credentials: "include",
        });
        if (response.ok) {
            const updatedAccount = await response.json();
            setAccount(updatedAccount);
            userContext.setUserData({...userContext.currentUser, username: updatedAccount.username});
        }
    };

    const deleteAccount = async () => {
        // soft delete the account (the account is still in the database but no longer active)
        const response = await fetch(`${Env.API_BASE_URL}/api/users/delete/${account.id}`, {
            method: "DELETE",
            credentials: "include",
        });
        if (response.ok) {
            // after deleting the user, we logout the account
            await fetch(`${Env.API_BASE_URL}/auth/logout`, {
                method: "POST",
                headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                },
                credentials: "include", // IMPORTANT for sending/receiving cookies
            });
            // we clear the user context
            userContext?.setUserData({
                id: -1,
                username: "",
                role: "ROLE_ANONYMOUS"
            });
            navigate("/");
        }
    };

    return (
        <div className="relative h-full w-full">
            <button className="border-2 border-gray-400 bg-white text-black font-bold text-lg rounded-sm absolute top-0 right-0 m-2 cursor-pointer" onClick={() => navigate("/")}>Go back</button>
            <div className="h-full w-full flex flex-col justify-center items-center bg-gradient-to-b from-slate-200 to-slate-300 p-2">
                <div className="h-9/10 w-3/4 bg-amber-100 border border-amber-200 rounded-xl shadow grid grid-cols-5">
                    <div className="col-span-1 col-start-1 h-full bg-gray-400 rounded-l-xl border-r-2 border-r-white grid grid-cols-1 grid-rows-6">
                        <div className="w-full row-start-1 row-span-2 flex-col flex items-center justify-evenly border-b-2 border-b-white">
                            <div className="h-25 w-25 rounded-full flex flex-row justify-center items-center bg-teal-300 border-gray-500 border-2">
                                <p className="font-bold text-6xl">{account.username.charAt(0).toUpperCase()}</p>
                            </div>
                            <p className="text-2xl font-bold">{account.username}</p>
                        </div>
                        <div className="w-full row-span-4 flex flex-col items-center justify-between">
                            <div className="flex flex-col justify-start gap-1 w-full">
                                <button className="bg-gray-600 text-white font-bold text-center cursor-pointer" onClick={()=>goToNestedRoute("settings")}>Informations</button>
                            </div>
                            <button className="w-full bg-red-700 hover:bg-red-600 text-xl font-bold rounded-bl-xl cursor-pointer py-2" onClick={()=>setDialogHandler({...dialogHandler, confirmDialog: true})}>Delete Account</button>
                            <ConfirmDialog open={dialogHandler.confirmDialog} close={()=>setDialogHandler({...dialogHandler, confirmDialog: false})} title={`Delete this Account?`} text={"Are you sure you want to delete this account?"} okButtonText="Remove" Icon={<AttentionSignLogo className="w-12 h-12 fill-red-600"/>} callback={deleteAccount}/>
                        </div>
                    </div>
                    <div className="col-span-4 h-full bg-gray-400/50 rounded-r-xl flex flex-col justify-center">
                        <Outlet context={{ account: account, refreshAccountData }}/>
                    </div>
                </div>
            </div>
        </div>
    );
}