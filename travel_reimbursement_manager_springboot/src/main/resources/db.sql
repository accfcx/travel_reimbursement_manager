
create table bank_card
(
    id          bigint       null,
    owner_id    bigint       null,
    card_number varchar(255) null,
    bank        varchar(255) null,
    area        varchar(255) null,
    create_time datetime     null,
    update_time datetime     null
);



create table daily_reimbursement
(
    id           bigint not null
        primary key,
    recipe_id    bigint null,
    bank_card_id bigint null,
    reason       text   null
);


create table department
(
    id                   int          null,
    parent_department_id int          null,
    name                 varchar(255) null
);


create table fee_item
(
    id           int            not null
        primary key,
    fee_type     varchar(255)   null,
    start_date   date           null,
    end_date     date           null,
    amount       decimal(10, 2) null,
    start_city   varchar(255)   null,
    enc_city     varchar(255)   null,
    purpose      varchar(512)   null,
    people_count int            null
);


create table invoice
(
    id             int            null,
    invoice_number varchar(255)   null,
    amount         decimal(10, 2) null,
    invoice_date   date           null,
    invoice_type   varchar(32)    null,
    file_path      varchar(255)   null,
    receipt_id     int            null
);

create table overtime_apply
(
    id         bigint not null
        primary key,
    recipe_id  bigint null,
    start_date date   null,
    end_date   date   null,
    hours      int    null,
    reason     text   null
);



create table Permission
(
    id   int          null,
    name varchar(255) null,
    role varchar(255) null
);

create table person
(
    id            int          null,
    username      varchar(255) null,
    name_zh       varchar(255) null,
    password      varchar(512) null,
    department_id int          null
);

create table process_def
(
    id           bigint auto_increment comment '模型编号'
        primary key,
    process_name varchar(255) null comment '模型名称',
    process_key  varchar(255) null comment '模型key',
    recipe_type  varchar(255) null comment '类型',
    process_xml  text         null comment '模型文件',
    submit_uid   bigint       null comment '提交人',
    update_uid   bigint       null comment '更新人',
    create_time  timestamp    null on update CURRENT_TIMESTAMP comment '创建时间',
    update_time  timestamp    null on update CURRENT_TIMESTAMP comment '修改时间',
    status       int          null comment '是否发布(1:发布;0:不发布)'
);



create table recipe
(
    id            bigint         not null
        primary key,
    no            varchar(255)   null,
    recipe_type   varchar(255)   null,
    amount        decimal(10, 2) null,
    uid           bigint         null,
    department_id bigint         null,
    reason        text           null,
    recipe_status varchar(255)   null
);


create table travel_apply
(
    id         bigint not null
        primary key,
    recipe_id  bigint null,
    start_date date   null,
    end_date   date   null,
    purpose    text   null
);


create table travel_reimbursement
(
    id             bigint not null
        primary key,
    recipe_id      bigint null,
    application_id bigint null,
    bank_card_id   bigint null,
    purpose        text   null
);