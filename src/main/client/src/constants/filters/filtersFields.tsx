import { fieldEnum, filterDataType, bookValuesType } from "../interfaces";

export const bookFilterFieldsData : filterDataType[] = [
    {
        id: "searchAuthor",
        type: fieldEnum.Text,
        label: "Author",
        value: "",
        condition: (a:bookValuesType, b: bookValuesType) => 
            {return (a as string).toLowerCase().includes((b as string).toLowerCase())},
        objKeyRef: "author"
    },
    {
        id: "searchGenres",
        type: fieldEnum.Select,
        label: "Genres",
        value: "",
        condition: (a:bookValuesType, b: bookValuesType) =>
            {return (a as string[]).every((element)=>(b as string[]).includes(element))},
        selectOptions: [
            {
                label: "---",
                value: ""
            },
            {
                label: "Fantasy",
                value: "fantasy"
            },
            {
                label: "Sci-Fi",
                value: "sci-fi"
            },
            {
                label: "History",
                value: "history"
            },
            {
                label: "Horror",
                value: "horror"
            },
            {
                label: "Detective",
                value: "detective"
            },
            {
                label: "Comedy",
                value: "comedy"
            },
        ],
        objKeyRef: "author"
    },
    {
        id: "searchReserved",
        type: fieldEnum.Check,
        label: "reserved",
        value: false,
        condition: (a:bookValuesType, b: bookValuesType) =>
             {return !(a as boolean) || (a as boolean) == (b as boolean)},
        objKeyRef: "author"
    },
    
];