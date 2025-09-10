package com.billykid.template.utils.parameters;

import com.billykid.template.utils.enums.UserRole;

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

public class UserParametersObject {
    String userName;
    String email;
    UserRole role;
    
}