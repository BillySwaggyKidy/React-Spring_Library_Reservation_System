import { fieldEnum, inputFieldValueType } from "./input";

export interface filterCommonDataType {
    id: string,
    label: string,
    value: inputFieldValueType,
    selectOptions?: {label:string,value:number|string}[]
};

export interface fieldFilterDataType extends filterCommonDataType {
    type: fieldEnum,
    filterCallback: (id : string, value: inputFieldValueType) => void
};

export interface filterDataType extends filterCommonDataType {
    type: fieldEnum,
    searchParamName: string,
};