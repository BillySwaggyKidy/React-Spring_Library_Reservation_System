import { Env } from "@/src/Env";
import { userDataType } from "@/src/types/user";
import { SubmitHandler, useForm } from "react-hook-form";
import { useOutletContext } from "react-router-dom";

interface IFormInput {
    username: string,
    email: string,
    password: string,
    confirmPassword: string,
}

// represent the nested page to edit the user info
export default function AccountSettings() {
    const { account, refreshAccountData } = useOutletContext<{account: userDataType;refreshAccountData: () => void;}>();
    const { register, handleSubmit, reset } = useForm<IFormInput>({
        defaultValues: {
            username: account.username,
            email: account.email,
            password: "",
            confirmPassword: "",
        }
    });


    const editAccount: SubmitHandler<IFormInput> = async (data : IFormInput) => {
        const response = await fetch(`${Env.API_BASE_URL}/auth/account/${account.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include", // IMPORTANT for sending/receiving cookies
            body: JSON.stringify(data),
        });
            
        if (response.ok) {
            const accountInfo : userDataType = await response.json();
            reset({
                username: accountInfo.username,
                email: accountInfo.email,
                password: "",
                confirmPassword: ""
            });
            refreshAccountData();
        } 
    }

    return (
        <div className="w-1/2 max-w-2xl mx-auto p-6 bg-gray-400 rounded-xl shadow-md">
            <h1 className="text-3xl font-bold mb-6 text-gray-800">Account Settings</h1>
            <form onSubmit={handleSubmit(editAccount)} className="space-y-6">
                {/* Username */}
                <div>
                    <label htmlFor="username" className="block text-sm font-medium text-gray-700">
                        Username
                    </label>
                    <input
                        id="username"
                        type="text"
                        {...register("username")}
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-200 bg-amber-900/20 pl-0.5"
                    />
                </div>

                {/* Email */}
                <div>
                    <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                        Email address
                    </label>
                    <input
                        id="email"
                        type="email"
                        {...register("email")}
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-200 bg-amber-900/20 pl-0.5"
                    />
                </div>

                {/* Change password */}
                <div className="border-t pt-4">
                    <h2 className="text-lg font-semibold text-gray-800 mb-2">Change Password</h2>

                    <div className="space-y-4">
                        <div>
                            <div>
                                <label htmlFor="new-password" className="block text-sm font-medium text-gray-700">
                                    New password
                                </label>
                                <input
                                    id="new-password"
                                    type="password"
                                    {...register("password")}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-200 bg-amber-900/20 pl-0.5"
                                />
                            </div>

                            <div>
                                <label htmlFor="confirm-password" className="block text-sm font-medium text-gray-700">
                                    Confirm new password
                                </label>
                                <input
                                    id="confirm-password"
                                    type="password"
                                    {...register("confirmPassword")}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-200 bg-amber-900/20 pl-0.5"
                                />
                            </div>
                        </div>
                    </div>
                </div>

                {/* Save button */}
                <div className="flex justify-end">
                    <button
                        type="submit"
                        className="px-6 py-2 bg-amber-700 text-white rounded-md shadow hover:bg-amber-600 transition cursor-pointer"
                    >
                        Save Changes
                    </button>
                </div>
            </form>
        </div>
    );
}
