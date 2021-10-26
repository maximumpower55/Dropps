package me.maximumpower55.example;

import net.fabricmc.api.ClientModInitializer;

public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Hello client world!!");
    }
}
