import { Env } from "@/src/Env";
import { editUserInfoType } from "@/src/types/user";
import { useEffect, useRef, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";

interface AdminIFormInput {
    username: string,
    email: string,
    password: string,
    confirmPassword: string,
    role: string
}

export default function EditUserModal({id, userInfo, open, close} : {id:number, userInfo : editUserInfoType, open: boolean, close: (refresh: boolean)=>void}) {
    const { register, handleSubmit } = useForm<AdminIFormInput>({
        defaultValues: {
            username: userInfo?.username,
            email: userInfo?.email,
            password: "",
            confirmPassword: "",
            role: userInfo?.role
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

    const editExistingUser: SubmitHandler<AdminIFormInput> = async (data : AdminIFormInput) => {
        const response = await fetch(`${Env.API_BASE_URL}/api/users/update/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include", // IMPORTANT for sending/receiving cookies
        body: JSON.stringify(data),
        });
        
        if (response.ok) {
            setErrorText("");
            close(true);
        } 
        else {
            let errorText = await response.text();
            errorText = errorText.substring(errorText.indexOf(":")+2);
            // Show error
            setErrorText(errorText);
        }
    };
    

    return (
        <dialog ref={ref} className="w-3/10 h-6/10 fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 grid grid-cols-1 grid-rows-7 backdrop:bg-black/60 animate-fade-in-scale bg-amber-700 border-2 border-white rounded-2xl p-2">
            <button 
                className=" font-modal-close-icon text-gray-400 font-bold text-4xl rounded-sm absolute top-0 right-0 px-2 bg-transparent cursor-pointer font-" onClick={()=>close(false)}>×</button>
            <div className="w-full row-start-1">
                <h1 className="font-bold text-4xl text-center">Edit User</h1>
            </div>
            <div className="w-full row-start-2 row-span-6 flex flex-col items-center">
                <form className="w-full flex flex-col justify-around items-center py-2" onSubmit={handleSubmit(editExistingUser)}>
                    <div className="w-full flex flex-col items-start">
                        <label className="text-2xl">Username</label>
                        <input className="rounded-md border-2 border-white bg-green-900/60 h-8 text-xl w-3/4 mb-2" {...register("username")} />
                    </div>
                    <div className="w-full flex flex-col items-start mb-2">
                        <label className="text-2xl">Email</label>
                        <input className="rounded-md border-2 border-white bg-green-900/60 h-8 text-xl w-3/4" type="email" {...register("email")} />
                    </div>
                    <div className="w-full flex flex-col items-start mb-2">
                        <label className="text-2xl">Password</label>
                        <input className="rounded-md border-2 border-white bg-green-900/60 h-8 text-xl w-3/4" type="password" {...register("password")} />
                    </div>
                    <div className="w-full flex flex-col items-start mb-2">
                        <label className="text-2xl">Confirm password</label>
                        <input className="rounded-md border-2 border-white bg-green-900/60 h-8 text-xl w-3/4" type="password" {...register("confirmPassword")} />
                    </div>
                    <div className="w-full flex flex-col items-start mb-2">
                        <label className="text-2xl">Role</label>
                        <select className="rounded-md border-2 border-white bg-green-900/60 h-8 text-xl w-3/4" {...register("role")}>
                            <option value="">---</option>
                            <option value="ROLE_CUSTOMER">Customer</option>
                            <option value="ROLE_EMPLOYEE">Employee</option>
                            <option value="ROLE_ADMIN">Admin</option>
                        </select>
                    </div>
                    <input className="mt-4 w-full sm:w-3/4 py-2 px-4 bg-orange-600 border-2 border-orange-800 rounded-lg text-white text-xl font-semibold transition-colors hover:bg-orange-700 active:bg-orange-500 cursor-pointer" type="submit" value={"Edit account"}/>
                </form>
                <div className="h-[10%]">
                    {
                        errorText.length > 0 &&
                        <div className="p-2 border-2 border-gray-500 rounded-lg bg-red-300/50 flex flex-row justify-center items-center">
                            <p className="text-center text-xl text-white font-bold">{errorText}</p>
                        </div>
                    }
                </div>
            </div>
        </dialog>
    );
}