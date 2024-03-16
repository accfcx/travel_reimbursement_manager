create table bank_card
(
    id          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    owner_id    bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
    card_number varchar(255)        NOT NULL DEFAULT '' COMMENT '银行卡号',
    bank        varchar(255)        NOT NULL DEFAULT '' COMMENT '银行名称',
    area        varchar(255)        NOT NULL DEFAULT '' COMMENT '银行地区',
    create_time timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_instance_code` (`owner_id`),
    UNIQUE KEY `uniq_card_number` (`card_number`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8 COMMENT ='银行卡信息表';


create table daily_reimbursement
(
    id           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    recipe_id    bigint(20) unsigned NOT NULL DEFAULT '0' comment '单据id',
    bank_card_id bigint(20) unsigned NOT NULL DEFAULT '0' comment '银行卡id',
    create_time  timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8 COMMENT ='日常报销单';

create table department
(
    id                   bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    parent_department_id bigint(20) unsigned NOT NULL DEFAULT '0' comment '上级部门id',
    name                 varchar(255)        NOT NULL DEFAULT '' comment '部门名称',
    leader_id            bigint                       default 0 not null comment '部门领导',
    create_time          timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time          timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='部门表';

create table fee_item
(
    id           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    recipe_id    bigint(20) unsigned NOT NULL DEFAULT '0' comment '单据id',
    fee_type     varchar(255)        NOT NULL DEFAULT '' comment '费用类型',
    start_date   date                null comment '开始日期',
    end_date     date                null comment '结束日期',
    amount       decimal(10, 2)      NOT NULL DEFAULT '0' comment '金额',
    start_city   varchar(255)        NOT NULL DEFAULT '' comment '出发地',
    enc_city     varchar(255)        NOT NULL DEFAULT '' comment '目的地',
    purpose      varchar(255)        NOT NULL DEFAULT '' comment '事由',
    people_count int                 NOT NULL DEFAULT '0' comment '人数',
    create_time  timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_fee_type` (`fee_type`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8 COMMENT ='费用项表';

create table invoice
(
    id             bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    invoice_number varchar(255)        NOT NULL DEFAULT '' COMMENT '发票代码',
    amount         decimal(10, 2)      NOT NULL DEFAULT '0' comment '金额',
    invoice_date   varchar(255)        NOT NULL DEFAULT '' comment '发票日期',
    invoice_type   varchar(255)        NOT NULL DEFAULT '' comment '发票类型',
    file_path      varchar(255)        NOT NULL DEFAULT '' comment '发票文件储存路径',
    recipe_id      bigint(20) unsigned NOT NULL DEFAULT '0' comment '单据id',
    create_time    timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_invoice_number` (`invoice_number`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8 COMMENT ='发票表';

create table overtime_apply
(
    id          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    recipe_id   bigint(20) unsigned NOT NULL DEFAULT '0' comment '单据id',
    start_date  datetime            null comment '开始日期',
    end_date    datetime            null comment '结束日期',
    hours       int                 NOT NULL DEFAULT '0' comment '加班时长(H)',
    create_time timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8 COMMENT ='加班申请表';


INSERT INTO test.overtime_apply (id, recipe_id, start_date, end_date, hours, create_time, update_time)
VALUES (1, 2, '2024-03-01 20:00:00', '2024-03-02 00:00:00', 4, '2024-03-01 22:18:26',
        '2024-03-14 22:58:18');

create table permission
(
    id          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    uid         bigint(20) unsigned NOT NULL COMMENT '员工id',
    role        varchar(255)        NOT NULL DEFAULT '' comment '权限角色',
    create_time timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8 COMMENT ='权限表';

create table person
(
    id            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    username      varchar(255)        NOT NULL DEFAULT '' comment '用户名称',
    name_zh       varchar(255)        NOT NULL DEFAULT '' comment '用户中文名称',
    password      varchar(255)        NOT NULL DEFAULT '' comment '密码',
    department_id bigint(20) unsigned NOT NULL DEFAULT '0' comment '部门id',
    create_time   timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8 COMMENT ='员工表';

INSERT INTO test.person (id, username, name_zh, password, department_id, create_time, update_time)
VALUES (1, 'zhangsan', '张三', '123456', 3, '2024-03-13 22:48:27', '2024-03-13 22:48:27');
INSERT INTO test.person (id, username, name_zh, password, department_id, create_time, update_time)
VALUES (2, 'zhaosi', '甘正国', '123456', 3, '2024-03-13 22:48:27', '2024-03-13 22:48:27');


create table process_def
(
    id                     bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    process_name           varchar(255)        NOT NULL DEFAULT '' comment '流程名称',
    process_key            varchar(255)        NOT NULL DEFAULT '' comment '流程key',
    recipe_type            varchar(255)        NOT NULL DEFAULT '' comment '业务类型',
    process_xml            text                null comment '流程定义',
    publish_process_xml    text                null comment '已发布流程定义',
    submit_uid             bigint(20) unsigned NOT NULL DEFAULT '0' comment '提交人',
    update_uid             bigint(20) unsigned NOT NULL DEFAULT '0' comment '更新人',
    status                 int                 NOT NULL DEFAULT '0' comment '是否发布(1:发布;0:不发布)',
    process_definition_key varchar(255)        NOT NULL DEFAULT '' comment '已发布流程key',
    process_definition_id  varchar(255)        not null default '' comment '已发布流程id',
    variables              text                null comment '编辑中的流程变量',
    publish_variables      text                null comment '已发布流程定义变量',
    create_time            timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time            timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_recipe_type` (`recipe_type`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8 COMMENT ='流程定义表';

INSERT INTO test.process_def (id, process_name, process_key, recipe_type, process_xml, publish_process_xml, submit_uid,
                              update_uid, status, process_definition_key, create_time, update_time)
VALUES (1, '直属领导审批', 'leader', 'travelApply', '', '', 1, 0, 0, '', '2024-03-14 23:38:26',
        '2024-03-14 23:49:01');

create table recipe
(
    id                  bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    no                  varchar(255)        NOT NULL DEFAULT '' comment '单据编号',
    recipe_type         varchar(255)        NOT NULL DEFAULT '' comment '单据类型',
    amount              decimal(10, 2)      NOT NULL DEFAULT '0' comment '金额',
    uid                 bigint(20) unsigned NOT NULL DEFAULT '0' comment '用户id',
    department_id       bigint(20) unsigned NOT NULL DEFAULT '0' comment '部门id',
    reason              text                null comment '事由',
    deny_detail         varchar(255)        NOT NULL DEFAULT '' comment '审批拒绝原因',
    recipe_status       varchar(255)        NOT NULL DEFAULT '' comment '审批状态',
    process_instance_id varchar(255)        NOT NULL DEFAULT '' comment '审批流程实例id',
    create_time         timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time         timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_no` (`no`),
    KEY `idx_recipe_type` (`recipe_type`),
    KEY `idx_uid` (`uid`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8 COMMENT ='单据基础信息表';

INSERT INTO test.recipe (id, no, recipe_type, amount, uid, department_id, reason, deny_detail, recipe_status,
                         process_instance_id, create_time, update_time)
VALUES (1, 'B202402291', 'dailyReimbursement', 285.52, 1, 3, '加班打车', '', 'unSubmit', '', '2024-03-13 22:49:25',
        '2024-03-14 22:10:34');
INSERT INTO test.recipe (id, no, recipe_type, amount, uid, department_id, reason, deny_detail, recipe_status,
                         process_instance_id, create_time, update_time)
VALUES (2, 'B202403012', 'overtimeApply', 0, 1, 3, '正常加班', '', 'unSubmit', '', '2024-03-01 22:14:44',
        '2024-03-02 22:14:46');


create table travel_apply
(
    id            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    recipe_id     bigint(20) unsigned NOT NULL DEFAULT '0' comment '单据id',
    apply_type    varchar(255)                 default '' not null comment '申请类型',
    travel_target varchar(255)                 default '' not null comment '差旅目的',
    start_date    datetime            null comment '开始日期',
    end_date      datetime            null comment '结束日期',
    create_time   timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    comment '差旅申请表';


create table travel_reimbursement
(
    id                      bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    recipe_id               bigint(20) unsigned NOT NULL DEFAULT '0' comment '单据id',
    related_travel_apply_id bigint                       default 0 not null comment '关联差旅申请单',
    bank_card_id            bigint(20) unsigned NOT NULL DEFAULT '0' comment '银行卡id',
    create_time             timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time             timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    comment '差旅报销表';