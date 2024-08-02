package ru.otus.highload.homework.fifth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.highload.homework.fifth.dto.MessageInDto;
import ru.otus.highload.homework.fifth.dto.MessageOutDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Value("${db.url}")
    private String url;

    @Value("${db.user}")
    private String user;

    @Value("${db.password}")
    private String password;

    @Value("${db.url.second}")
    private String urlSecond;

    @Value("${db.user.second}")
    private String userSecond;

    @Value("${db.password.second}")
    private String passwordSecond;

    @Value("${db.shard}")
    private Long shard1;

    @Value("${db.shard.second}")
    private Long shard2;

    private final String ADD_MESSAGE =
            "INSERT INTO messages (chat_id, user_from_id, user_to_id, message) VALUES (?,?,?,?);";

    private final String GET_MESSAGES =
            "SELECT * FROM messages WHERE chat_id = ?;";


    public List<MessageOutDto> getMessages(Long chatId) {
        if (chatId % shard2 < shard1) {
            return getMessages(chatId, url + "?currentSchema=otus_5", user, password);
        } else {
            return getMessages(chatId, urlSecond + "?currentSchema=otus_5", userSecond, passwordSecond);
        }
    }

    private List<MessageOutDto> getMessages(Long chatId, String urlSecond, String userSecond, String passwordSecond) {
        List<MessageOutDto> messageOutDtos = new ArrayList<>();

        try (Connection connection =
                     DriverManager.getConnection(urlSecond, userSecond, passwordSecond);) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_MESSAGES);
            preparedStatement.setLong(1, chatId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                messageOutDtos.add(new MessageOutDto(resultSet.getLong("user_from_id"),
                        resultSet.getLong("user_to_id"),
                        resultSet.getString("message"),
                        resultSet.getTimestamp("create_datetime")));

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return messageOutDtos;
    }

    public void addMessage(MessageInDto dto) {
        if (dto.chatId() % shard2 < shard1) {
            createMessage(dto, url + "?currentSchema=otus_5", user, password);
        } else {
            createMessage(dto, urlSecond + "?currentSchema=otus_5", userSecond, passwordSecond);
        }
    }

    private void createMessage(MessageInDto dto, String urlSecond, String userSecond, String passwordSecond) {
        try (Connection connection =
                     DriverManager.getConnection(urlSecond, userSecond, passwordSecond);) {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_MESSAGE);
            preparedStatement.setLong(1, dto.chatId());
            preparedStatement.setLong(2, dto.userFromId());
            preparedStatement.setLong(3, dto.userToId());
            preparedStatement.setString(4, dto.message());
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
