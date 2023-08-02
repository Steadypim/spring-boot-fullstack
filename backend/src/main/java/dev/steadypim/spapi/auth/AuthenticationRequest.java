package dev.steadypim.spapi.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
