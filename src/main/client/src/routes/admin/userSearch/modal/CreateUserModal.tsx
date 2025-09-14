import { Env } from "@/src/Env";
import { useEffect, useRef, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";

interface AdminIFormInput {
    username: string,
    email: string,
    password: string,
    confirmPassword: string,
    role: string
}

export default function CreateUserModal({open, close} : {open: boolean, close: (refresh: boolean)=>void}) {
    const { register, handleSubmit, getValues, formState: { errors } } = useForm<AdminIFormInput>();
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

    const createNewUser: SubmitHandler<AdminIFormInput> = async (data : AdminIFormInput) => {
        const response = await fetch(`${Env.API_BASE_URL}/api/users/add`, {
            method: "POST",
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
        <dialog ref={ref} className="w-3/10 h-8/10 fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 grid grid-cols-1 grid-rows-7 backdrop:bg-black/60 animate-fade-in-scale bg-green-700 border-2 border-white rounded-2xl p-2">
            <button 
                className=" font-modal-close-icon text-gray-400 font-bold text-4xl rounded-sm absolute top-0 right-0 px-2 bg-transparent cursor-pointer font-" onClick={()=>close(false)}>×</button>
            <div className="w-full row-start-1 flex flex-col justify-center items-center">
                <h1 className="font-bold text-4xl text-center">Create new User</h1>
            </div>
            <div className="w-full row-start-2 row-span-6 flex flex-col items-center">
                <form className="w-full flex flex-col justify-around items-center py-2" onSubmit={handleSubmit(createNewUser)}>
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
                        <input className="rounded-md border-2 border-white bg-green-900/60 h-8 text-xl w-3/4" type="password" {...register("password", {
                            required: "Password is required",
                            minLength: {
                            value: 6,
                            message: "Password must be at least 6 characters"
                            },
                            pattern: {
                            value: /^(?=.*[0-9])(?=.*[!@#$%^&*(),.?":{}|<>]).*$/,
                            message: "Password must include at least one number and one special character",
                            }
                        })} />
                        {errors.password && <span className="text-red-500">{errors.password.message}</span>}
                    </div>
                    <div className="w-full flex flex-col items-start mb-2">
                        <label className="text-2xl">Confirm password</label>
                        <input className="rounded-md border-2 border-white bg-green-900/60 h-8 text-xl w-3/4" type="password" {...register("confirmPassword", {
                            required: "Please confirm your password",
                            validate: value => {
                            const password = getValues("password");
                            return value === password;
                        }})}/>
                        {errors.confirmPassword && <span className="text-red-500">{errors.confirmPassword.message}</span>}
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
                    <input className="mt-4 w-full sm:w-3/4 py-2 px-4 bg-green-600 border-2 border-green-800 rounded-lg text-white text-xl font-semibold transition-colors hover:bg-green-700 active:bg-grenn-500 cursor-pointer" type="submit" value={"Create new account"}/>
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