package com.scm.forms;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {
    @NotBlank(message = "User name is Required")
    @Size(min=3,message = "Min 3 characters are required")
    private String name;
    @Email(message  ="Invalid Email adress")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min=6,message = "Min 6 Charcters is required")
    private  String password;
    @NotBlank(message = "About is Required")
    private String about;
    @Size(min=8,max=12,message = "Invalid Phone Number")
    private String phoneNumber;
}
