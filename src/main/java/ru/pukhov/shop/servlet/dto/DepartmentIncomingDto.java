package ru.pukhov.shop.servlet.dto;

public class DepartmentIncomingDto {
    private String name;

    public DepartmentIncomingDto() {
    }

    public DepartmentIncomingDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
