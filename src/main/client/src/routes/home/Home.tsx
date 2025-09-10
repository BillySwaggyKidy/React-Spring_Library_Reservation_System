import Navbar from "./navbar/Navbar";
import { Outlet } from "react-router-dom";

export default function Home() {

    return (
        <div className="h-full w-full flex flex-col justify-start">
            <Navbar/>
            <Outlet/>
        </div>
    )
};