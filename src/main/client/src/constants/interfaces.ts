enum fieldType {
    Text = "Text",
    Number = "Number",
    Check = "Check",
    Select = "Select"
}

interface filterFieldType {
    id: string,
    type: fieldType,
    label: string,
    value: string | boolean | number | string[],
    condition: 
    ((a:string,b:string)=>boolean) | 
    ((a:number,b:number)=>boolean) | 
    ((a:boolean,b:boolean)=>boolean) | 
    ((a:string[],b:string[])=>boolean),
    ref: string,
};

interface bookType {
    id: string,
    title: string,
    bookCoverUrl: string,
    description: string,
    genres: string[],
    author: string,
    date: Date,
    reserved: boolean
}

export {fieldType};
export type {filterFieldType, bookType};