package com.billykid.template.utils.parameters;

import java.time.LocalDate;

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

public class ReservationParametersObject {
    String userName;
    LocalDate beginDate;
    LocalDate endDate;
    
}
