import { fieldEnum, fieldFilterDataType, filterCommonDataType } from "@/src/constants/interfaces.ts";
import CheckFilter from "./fields/CheckFilter.tsx";
// import NumberFilter from "./fields/NumberFilter.tsx";
import SelectFilter from "./fields/SelectFilter.tsx";
import TextFilter from "./fields/TextFilter.tsx";

export default function FieldFilter({type, id, label, value, selectOptions, filterCallback} : fieldFilterDataType) {

    const renderFilterType = (type : fieldEnum, filterData : filterCommonDataType) => {
        switch(type) {
            case "Text": {
                const textValue = filterData.value as string;
                return <TextFilter {...filterData} value={textValue} filterCallback={filterCallback}/>;
            }
            case "Check": {
                const checkValue = filterData.value as boolean;
                return <CheckFilter {...filterData} value={checkValue} filterCallback={filterCallback}/>;
            }
            case "Select": {
                const selectValue = filterData.value as number|string;
                const options = filterData.selectOptions!;
                return <SelectFilter {...filterData} selectOptions={options} value={selectValue} filterCallback={filterCallback}/>;
            }
            // case "Number": {
            //     const numberValue = filterData.value as number;
            //     return <NumberFilter {...filterData} value={numberValue} filterCallback={filterCallback}/>;
            // }
        }
    }

    return renderFilterType(type, {id, label, value, selectOptions});
};