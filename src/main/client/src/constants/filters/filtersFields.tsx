import { filterDataType } from "@/src/types/filter";
import { fieldEnum } from "@/src/types/input";

export const bookFilterFieldsData : filterDataType[] = [
    {
        id: "searchTitle",
        type: fieldEnum.Text,
        label: "Title",
        value: "",
        searchParamName: "title"
    },
    {
        id: "searchAuthor",
        type: fieldEnum.Text,
        label: "Author",
        value: "",
        searchParamName: "author"
    },
    {
        id: "searchGenres",
        type: fieldEnum.Select,
        label: "Genres",
        value: "",
        selectOptions: [
            {
                label: "---",
                value: ""
            },
            {
                label: "Fantasy",
                value: "Fantasy"
            },
            {
                label: "Sci-Fi",
                value: "Sci-Fi"
            },
            {
                label: "History",
                value: "History"
            },
            {
                label: "Horror",
                value: "Horror"
            },
            {
                label: "Detective",
                value: "Detective"
            },
            {
                label: "Comedy",
                value: "Comedy"
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
        searchParamName: "genres"
    },
    {
        id: "searchReserved",
        type: fieldEnum.Check,
        label: "Reserved",
        value: false,
        searchParamName: "isReserved"
    },
    
];

export const userFilterFieldsData : filterDataType[] = [
    {
        id: "searchUsername",
        type: fieldEnum.Text,
        label: "Username",
        value: "",
        searchParamName: "username"
    },
    {
        id: "searchEmail",
        type: fieldEnum.Text,
        label: "Email",
        value: "",
        searchParamName: "email"
    },
    {
        id: "searchRole",
        type: fieldEnum.Select,
        label: "Role",
        value: "",
        selectOptions: [
            {
                label: "---",
                value: ""
            },
            {
                label: "Customer",
                value: "ROLE_CUSTOMER"
            },
            {
                label: "Employee",
                value: "ROLE_EMPLOYEE"
            },
            {
                label: "Admin",
                value: "ROLE_ADMIN"
            },
        ],
        searchParamName: "role"

    }
];