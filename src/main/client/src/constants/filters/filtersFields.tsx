import { filterDataType } from "@/src/types/filter";
import { fieldEnum } from "@/src/types/input";

export const bookFilterFieldsData : filterDataType[] = [
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