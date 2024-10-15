import { Env } from "@/src/Env";
import { useState } from "react";

export default function Login() {
    const [word, setWord] = useState("");

    const fetchPing = () => {
        fetch(`${Env.API_BASE_URL}/ping`)
        .then(response => response.text())
        .then(body => setWord(body));
    }

    return (
        <div className="h-full w-full flex flex-row justify-center items-center bg-slate-800">
            <div style={{ fontSize: 150 }}>🍰</div>
            <button onClick={fetchPing}>Ping</button>
            <p className="font-bold text-white">{word}</p>
        </div>
    );
};