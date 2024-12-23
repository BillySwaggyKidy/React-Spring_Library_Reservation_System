import { bookValuesType } from "@/src/constants/interfaces";
import { ChangeEvent, useState } from "react";

export default function TextFilter({id, label, value, filterCallback} : {id : string, label: string, value: string, filterCallback : (id : string, value: bookValuesType) => void}) {
    const [text, setText] = useState<string>(value);

    const handleText = (e : ChangeEvent<HTMLInputElement>) => {
        const newText = e.currentTarget.value;
        setText(newText);
        filterCallback(id, newText);
    };

    return (
        <div className="flex flex-col justify-center items-start">
            <label htmlFor={`${id}-text`} className="text-black">{label}</label>
            <input id={`${id}-text`} className="w-32 h-8 border-2 rounded-md bg-gray-400 outline-none focus:border-gray-600" type="text" value={text} onChange={handleText}/>
        </div>
    );
};