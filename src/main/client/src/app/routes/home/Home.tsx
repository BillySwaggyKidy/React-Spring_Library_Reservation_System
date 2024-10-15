import Dashboard from "./dashboard/Dashboard";
import Navbar from "./navbar/Navbar";

export default function Home() {

    return (
        <div className="h-full w-full bg-white_wood_BG bg-cover">
            <div className="h-full flex flex-col justify-start">
                <Navbar/>
                <Dashboard/>
            </div>
        </div>
    )
};