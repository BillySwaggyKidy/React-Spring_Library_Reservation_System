import { inputFieldValueType } from "@/src/types/input";
import { ChangeEvent, useState } from "react";

export default function NumberField({id, label, value, callback} : {id : string, label: string, value: number, callback : (id : string, value: inputFieldValueType) => void}) {
    const [number, setNumber] = useState<number>(value);

    const handleNumber = (event : ChangeEvent<HTMLInputElement>) => {
        const newNumber = +event.currentTarget.value;
        setNumber(newNumber);
        callback(id, newNumber);
    };

    return (
        <div className="w-full flex flex-col justify-center items-start">
            <label htmlFor={`${id}-text`}>{label}</label>
            <input id={`${id}-text`} className="w-full h-4 border-2 rounded-md pl-2" type="number" value={number} onChange={handleNumber}/>
        </div>
    );
};