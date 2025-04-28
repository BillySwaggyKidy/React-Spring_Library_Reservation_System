import { SubmitHandler, useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../context/userContext.tsx";
import { useContext, useState } from "react";
import { userDataType } from "@/src/constants/interfaces.ts";
import { Env } from "@/src/Env.ts";

interface IFormInput {
    username: string,
    password: string
  }

export default function Login() {
    const { register, handleSubmit } = useForm<IFormInput>();
    const [errorText, setErrorText] = useState<string>("");
    const navigate = useNavigate();
    const userContext = useContext(UserContext);

    const onSubmit: SubmitHandler<IFormInput> = async (data : IFormInput) => {
      const response = await fetch(`${Env.API_BASE_URL}/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include", // IMPORTANT for sending/receiving cookies
        body: JSON.stringify(data),
      });
        
      if (response.ok) {
        // Save state, redirect, show badge, etc
        const userData : userDataType = await response.json();
        userContext?.setUserData(userData);
        setErrorText("");
        navigate("/");
      } 
      else {
        // Show error
        setErrorText("Login failed");
      }
    };
  
    return (
      <div className="h-full w-full">
        <button className="border-2 border-gray-400 bg-white text-black font-bold text-lg rounded-sm absolute top-0 right-0 m-2" onClick={() => navigate("/")}>Go back</button>
        <div className="h-full w-full flex flex-col justify-center items-center bg-slate-800">
          <h2 className="text-6xl font-bold text-violet-600 mb-4">Login</h2>
          <form className="w-1/3 flex flex-col justify-around items-center border-4 rounded-lg bg-indigo-900/50 border-indigo-400 shadow-lg shadow-slate-400 px-8 py-2" onSubmit={handleSubmit(onSubmit)}>
            <div className="w-full flex flex-col items-start mb-2">
              <label className="text-2xl">Username</label>
              <input className="rounded-md h-8 text-xl w-3/4" {...register("username")} />
            </div>
            <div className="w-full flex flex-col items-start mb-2">
              <label className="text-2xl">Password</label>
              <input className="rounded-md h-8 text-xl w-3/4" type="password" {...register("password")} />
            </div>
            <input className="border-2 border-violet-800 bg-violet-600 hover:bg-violet-700 active:bg-violet-500 hover:cursor-pointer text-white text-xl p-2 rounded-lg" type="submit" value={"login"}/>
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