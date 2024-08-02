package ru.otus.highload.homework.fifth.dto;

import java.sql.Timestamp;

public record MessageOutDto(Long from, Long to, String message, Timestamp timestamp) {
}
