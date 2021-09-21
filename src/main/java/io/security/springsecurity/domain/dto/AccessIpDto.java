package io.security.springsecurity.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessIpDto{

    private Long id;
    private String ipAddress;

}
