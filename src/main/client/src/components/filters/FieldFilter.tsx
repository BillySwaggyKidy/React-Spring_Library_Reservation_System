import { fieldFilterDataType, filterCommonDataType } from "@/src/types/filter";
import { fieldEnum } from "@/src/types/input";
import TextField from "@/src/components/inputs/TextField";
import SelectField from "@/src/components/inputs/SelectField";
import CheckSection from "@/src/components/inputs/CheckSection";

export default function FieldFilter({type, id, label, value, selectOptions, filterCallback} : fieldFilterDataType) {

    const renderFilterType = (type : fieldEnum, filterData : filterCommonDataType) => {
        switch(type) {
            case "Text": {
                const textValue = filterData.value as string;
                return <TextField key={id} {...filterData} value={textValue} callback={filterCallback}/>;
            }
            case "Check": {
                const checkValue = filterData.value as boolean;
                return <CheckSection key={id} {...filterData} value={checkValue} callback={filterCallback}/>;
            }
            case "Select": {
                const selectValue = filterData.value as number|string;
                const options = filterData.selectOptions!;
                return <SelectField key={id} {...filterData} selectOptions={options} value={selectValue} callback={filterCallback}/>;
            }
            // case "Number": {
            //     const numberValue = filterData.value as number;
            //     return <NumberFilter {...filterData} value={numberValue} filterCallback={filterCallback}/>;
            // }
        }
    }

    return renderFilterType(type, {id, label, value, selectOptions});
};