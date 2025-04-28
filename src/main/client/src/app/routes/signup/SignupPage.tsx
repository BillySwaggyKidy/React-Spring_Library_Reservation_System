import { Env } from "@/src/Env";
import { useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";

interface IFormInput {
    username: string,
    email: string,
    password: string,
    confirmPassword: string
  }

export default function Signup() {
    const { register, handleSubmit } = useForm<IFormInput>();
    const [errorText, setErrorText] = useState<string>("");
    const navigate = useNavigate();

    const onRegister: SubmitHandler<IFormInput> = async (data : IFormInput) => {
      const response = await fetch(`${Env.API_BASE_URL}/auth/signup`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include", // IMPORTANT for sending/receiving cookies
        body: JSON.stringify(data),
      });
        
      if (response.ok) {
        setErrorText("");
        navigate("/");
      } 
      else {
        // Show error
        setErrorText("Couldn't create your account, please try later");
      }
    };
  
    return (
      <div className="h-full w-full">
        <button className="border-2 border-gray-400 bg-white text-black font-bold text-lg rounded-sm absolute top-0 right-0 m-2" onClick={() => navigate("/")}>Go back</button>
        <div className="h-full w-full flex flex-col justify-center items-center bg-slate-800">
          <h2 className="text-6xl font-bold text-orange-600 mb-4">Signup</h2>
          <form className="w-1/3 flex flex-col justify-around items-center border-4 rounded-lg bg-red-900/50 border-amber-400 shadow-lg shadow-slate-400 px-8 py-2" onSubmit={handleSubmit(onRegister)}>
            <div className="w-full flex flex-col items-start">
              <label className="text-2xl">Username</label>
              <input className="rounded-md h-8 text-xl w-3/4 mb-2" {...register("username")} />
            </div>
            <div className="w-full flex flex-col items-start mb-2">
              <label className="text-2xl">Email</label>
              <input className="rounded-md h-8 text-xl w-3/4" type="email" {...register("email")} />
            </div>
            <div className="w-full flex flex-col items-start mb-2">
              <label className="text-2xl">Password</label>
              <input className="rounded-md h-8 text-xl w-3/4" type="password" {...register("password")} />
            </div>
            <div className="w-full flex flex-col items-start mb-2">
              <label className="text-2xl">Confirm password</label>
              <input className="rounded-md h-8 text-xl w-3/4" type="password" {...register("confirmPassword")} />
            </div>
            <input className="border-2 border-orange-800 bg-orange-600 hover:bg-orange-700 active:bg-orange-500 hover:cursor-pointer text-white text-xl p-2 rounded-lg" type="submit" value={"Create new account"}/>
          </form>
          <div className="h-[10%]">
            {
              errorText.length > 0 &&
              <div className="mt-4 p-2 border-2 border-gray-500 rounded-lg bg-red-300/50 flex-row justify-center items-center">
                <p className="text-center text-xl text-white font-bold">{errorText}</p>
              </div>
            }
          </div>
        </div>
      </div>
    )
};