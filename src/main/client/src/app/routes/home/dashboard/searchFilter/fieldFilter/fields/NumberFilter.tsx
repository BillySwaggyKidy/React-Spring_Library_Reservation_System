import { bookValuesType } from "@/src/constants/interfaces";
import { ChangeEvent, useState } from "react";

export default function NumberFilter({id, label, value, filterCallback} : {id : string, label: string, value: number, filterCallback : (id : string, value: bookValuesType) => void}) {
    const [number, setNumber] = useState<number>(value);

    const handleNumber = (e : ChangeEvent<HTMLInputElement>) => {
        const newNumber = +e.currentTarget.value;
        setNumber(newNumber);
        filterCallback(id, newNumber);
    };

    return (
        <>
            <label htmlFor={`${id}-text`}>{label}</label>
            <input id={`${id}-text`} className="w-6 h-4 border-2 rounded-md" type="number" value={number} onChange={handleNumber}/>
        </>
    );
};