package ru.akirakozov.sd.refactoring.model;

import ru.akirakozov.sd.refactoring.common.Faker;

public class ProductFaker {
    public static Product getProduct() {
        return new Product(Faker.getId(), Faker.getName(), Faker.getPrice());
    }
}
