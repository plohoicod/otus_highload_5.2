package ru.otus.highload.homework.fifth.dto;

public record MessageInDto(String requestId, Long chatId, Long userFromId, Long userToId, String message) {
}
