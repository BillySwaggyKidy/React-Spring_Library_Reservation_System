import { bookValuesType } from "@/src/constants/interfaces";
import { ChangeEvent, useState } from "react";

export default function SelectFilter({id, label, value, selectOptions, filterCallback} : {id : string, label: string, value: number|string, selectOptions: {label:string,value:number|string}[], filterCallback : (id : string, value: bookValuesType) => void}) {
    const [selectValue, setSelectValue] = useState<string|number>(value);

    const handleSelect = (event : ChangeEvent<HTMLSelectElement>) => {
        const newSelectValue = event.target.value;
        setSelectValue(newSelectValue);
        filterCallback(id, newSelectValue);
    };

    return (
        <div className="flex flex-col justify-center items-start">
            <label className="text-black" htmlFor={`${id}-box`}>{label}</label>

            <select className="w-32 h-8 border-2 rounded-md bg-gray-400 outline-none focus:border-gray-600" name="pets" value={selectValue} id={`${id}-box`} onChange={handleSelect}>
                {
                    selectOptions.map((option)=>
                        <option key={label} value={option.value}>{option.label}</option>
                    )
                }
            </select>
        </div>
    );
};