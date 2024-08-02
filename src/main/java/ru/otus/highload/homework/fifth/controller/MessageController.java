package ru.otus.highload.homework.fifth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.highload.homework.fifth.dto.MessageInDto;
import ru.otus.highload.homework.fifth.dto.MessageOutDto;
import ru.otus.highload.homework.fifth.service.MessageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/message/add")
    @Operation(summary = "Добавление сообщения в чат")
    public void registerUser(@RequestBody MessageInDto messageInDto) {
        messageService.addMessage(messageInDto);
    }


    @GetMapping("/messages/get/{id}")
    @Operation(summary = "Получение сообщений чата")
    public List<MessageOutDto> getMessages(@PathVariable Long id) {
        return messageService.getMessages(id);
    }
}
