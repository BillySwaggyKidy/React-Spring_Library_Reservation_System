import { fieldType, filterFieldType } from "../interfaces";

export const bookFilterFieldsData : filterFieldType[] = [
    {
        id: "searchAuthor",
        type: fieldType.Text,
        label: "Search author",
        value: "",
        condition: (a: string, b: string) => {return a.toLowerCase().includes(b.toLowerCase())},
        ref: "author"
    },
    {
        id: "searchGenres",
        type: fieldType.Select,
        label: "Search genres",
        value: "",
        condition: (a: string[], b: string[]) => {return a.every((element)=>b.includes(element))},
        ref: "author"
    },
    {
        id: "searchReserved",
        type: fieldType.Check,
        label: "Not reserved only ",
        value: false,
        condition: (a: boolean, b: boolean) => {return a == b},
        ref: "author"
    },
    
];