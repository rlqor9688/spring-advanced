package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Length(min=8)
    @Pattern(regexp = ".*\\d.*")
    @Pattern(regexp = ".*[A-Z].*")
    private String newPassword;
}
