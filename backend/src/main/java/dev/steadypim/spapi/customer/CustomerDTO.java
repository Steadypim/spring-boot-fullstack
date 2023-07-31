package dev.steadypim.spapi.customer;

import java.util.List;

public record CustomerDTO(
        Integer id,
        String email,
        String name,
        Gender gender,
        Integer age,
        List<String> roles,
        String username
) {}
