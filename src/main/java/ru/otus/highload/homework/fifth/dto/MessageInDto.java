package ru.otus.highload.homework.fifth.dto;

public record MessageInDto(Long chatId, Long userFromId, Long userToId, String message) {
}
