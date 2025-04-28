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
            {
                label: "Tutorial",
                value: "Tutorial"
            },
            {
                label: "Children",
                value: "Children"
            },
            {
                label: "Videos games",
                value: "Videos games"
            },
        ],
        objKeyRef: "genres"
    },
    {
        id: "searchReserved",
        type: fieldEnum.Check,
        label: "Reserved",
        value: false,
        condition: (a:bookValuesType, b: bookValuesType) =>
             {return !(a as boolean) || (a as boolean) == (b as boolean)},
        objKeyRef: "isReserved"
    },
    
];