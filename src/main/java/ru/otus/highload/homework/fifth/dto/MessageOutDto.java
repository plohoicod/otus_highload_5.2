package ru.otus.highload.homework.fifth.dto;

import java.sql.Timestamp;

public record MessageOutDto(String requestId, Long from, Long to, String message, Timestamp timestamp) {
}
