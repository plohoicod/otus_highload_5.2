create table if not exists messages
(
    id bigserial not null,
    chat_id bigint not null,
    user_from_id bigint not null,
    user_to_id bigint not null,
    message text,
    create_datetime timestamp default CURRENT_TIMESTAMP not null,
    primary key (id)
);

CREATE INDEX chat_id_idx ON messages USING btree (chat_id);