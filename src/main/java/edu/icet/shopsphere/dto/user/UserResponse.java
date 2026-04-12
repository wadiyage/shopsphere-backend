package edu.icet.shopsphere.dto.user;

import edu.icet.shopsphere.dto.address.AddressResponse;
import edu.icet.shopsphere.entity.enums.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;

    private String firstName;
    private String lastName;

    private String email;

    private Role role;

    private List<AddressResponse> addresses;

    private LocalDateTime createdAt;
}
