enum fieldEnum {
    Text = "Text",
    Number = "Number",
    Check = "Check",
    Select = "Select"
};

interface bookType {
    id: string,
    title: string,
    bookCoverUrl: string,
    description: string,
    genres: string[],
    author: string,
    date: Date,
    volume: number,
    reserved: boolean
};

type bookValuesType = bookType[keyof bookType];

interface filterCommonDataType {
    id: string,
    label: string,
    value: bookValuesType,
    selectOptions?: {label:string,value:number|string}[]
};

interface fieldFilterDataType extends filterCommonDataType {
    type: fieldEnum,
    filterCallback: (id : string, value: bookValuesType) => void
};

interface filterDataType extends filterCommonDataType {
    type: fieldEnum,
    condition: ((a: bookValuesType, b: bookValuesType)=>boolean),
    objKeyRef: string,
};

export {fieldEnum};
export type {filterDataType, fieldFilterDataType, filterCommonDataType, bookType, bookValuesType};