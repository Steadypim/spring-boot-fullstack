package dev.steadypim.spapi.auth;

import dev.steadypim.spapi.customer.CustomerDTO;

public record AuthenticationResponse(
        CustomerDTO customerDTO,
        String token
) {
}
