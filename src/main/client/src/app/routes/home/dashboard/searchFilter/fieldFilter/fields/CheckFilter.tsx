import { bookValuesType } from "@/src/constants/interfaces";
import { useState } from "react";

export default function CheckFilter({id, label, value, filterCallback} : {id : string, label: string, value: boolean, filterCallback : (id : string, value: bookValuesType) => void}) {
    const [checked, setChecked] = useState<boolean>(value);

    const handleCheck = () => {
        const newValue = !checked;
        setChecked(newValue);
        filterCallback(id, newValue);
    };

    return (
        <div className={`w-24 h-8 flex flex-row justify-center items-center border-2 rounded-md ${checked ? "bg-green-500 border-green-700" : "bg-red-400 border-red-600"} cursor-pointer`} onClick={handleCheck}>
            <p className={`text-white font-bold ${!checked && "line-through"} select-none`}>{label}</p>
        </div>
    );
};