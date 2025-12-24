
drop database if exists `spring_ai_alibaba` default charset=utf8mb4;

create database if not exists `spring_ai_alibaba` default charset=utf8mb4;

-- drop table if exists `t_model_config`;

create table if not exists `t_model_config` (
    id bigint not null primary key auto_increment,
    model_name varchar(50) not null,
    provider_name varchar(50) not null,
    base_url varchar(200) not null,
    api_key varchar(200) not null
) default charset=utf8mb4;


-- drop table if exists `t_chat_history`;

create table if not exists `t_chat_history` (
    id bigint not null primary key auto_increment,


) default charset=utf8mb4;