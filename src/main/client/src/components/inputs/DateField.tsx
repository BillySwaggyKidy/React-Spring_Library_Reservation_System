import { inputFieldValueType } from "@/src/types/input";
import { ChangeEvent, useState } from "react";

export default function TextField({id, label, value, callback} : {id : string, label: string, value: string, callback : (id : string, value: inputFieldValueType) => void}) {
    const [date, setDate] = useState<string>(value);

    const handleDate = (event : ChangeEvent<HTMLInputElement>) => {
        const newValue = event.currentTarget.value;
        setDate(newValue);
        callback(id, newValue);
    };

    return (
        <div className="w-full flex flex-col justify-center items-start">
            <label htmlFor={`${id}-text`} className="text-black">{label}</label>
            <input id={`${id}-text`} className="w-full h-8 border-2 rounded-md bg-gray-400 outline-none focus:border-gray-600 pl-2" type="date" value={date} onChange={handleDate}/>
        </div>
    );
};