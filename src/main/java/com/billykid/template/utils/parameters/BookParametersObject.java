package com.billykid.template.utils.parameters;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookParametersObject {
    private String title;
    private String author;
    private List<String> genres;
    private boolean isReserved;
}
