package dev.steadypim.spapi.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
