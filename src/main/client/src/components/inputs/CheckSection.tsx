import { inputFieldValueType } from "@/src/types/input";
import { useState } from "react";

export default function CheckSection({id, label, value, callback} : {id : string, label: string, value: boolean, callback : (id : string, value: inputFieldValueType) => void}) {
    const [checked, setChecked] = useState<boolean>(value);

    const handleCheck = () => {
        const newValue = !checked;
        setChecked(newValue);
        callback(id, newValue);
    };

    return (
        <div className={`w-full h-8 flex flex-row justify-center items-center border-2 rounded-md ${checked ? "bg-green-500 border-green-700" : "bg-red-400 border-red-600"} cursor-pointer`} onClick={handleCheck}>
            <p className={`text-white font-bold ${!checked && "line-through"} select-none`}>{label}</p>
        </div>
    );
};