import { inputFieldValueType } from "@/src/types/input";
import { ChangeEvent, useState } from "react";

export default function SelectField({id, label, value, selectOptions, callback} : {id : string, label: string, value: number|string, selectOptions: {label:string,value:number|string}[], callback : (id : string, value: inputFieldValueType) => void}) {
    const [selectValue, setSelectValue] = useState<string|number>(value);

    const handleSelect = (event : ChangeEvent<HTMLSelectElement>) => {
        const newSelectValue = event.currentTarget.value;
        setSelectValue(newSelectValue);
        callback(id, newSelectValue);
    };

    return (
        <div className="w-full flex flex-col justify-center items-start">
            <label className="text-black" htmlFor={`${id}-box`}>{label}</label>

            <select className="w-full h-8 border-2 rounded-md bg-gray-400 outline-none focus:border-gray-600 pl-2" value={selectValue} id={`${id}-box`} onChange={handleSelect}>
                {
                    selectOptions.map((option)=>
                        <option key={option.label} value={option.value}>{option.label}</option>
                    )
                }
            </select>
        </div>
    );
};