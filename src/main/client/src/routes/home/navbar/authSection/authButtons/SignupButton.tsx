import { useNavigate } from "react-router-dom";


export default function SignupButton() {
    const navigate = useNavigate();

    return (
        <button className="m-2 p-2 bg-orange-600 hover:bg-orange-400 active:bg-orange-500 transition-colors text-white font-bold border-2 border-solid border-orange-800 rounded-md cursor-pointer" onClick={() => navigate("/signup")}>
            Signup
        </button>
    );
}