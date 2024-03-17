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
    end_city     varchar(255)        NOT NULL DEFAULT '' comment '目的地',
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



INSERT INTO test.department (id, parent_department_id, name, leader_id, create_time, update_time) VALUES (1, 0, '总裁', 6, '2024-03-13 22:48:01', '2024-03-16 19:40:48');
INSERT INTO test.department (id, parent_department_id, name, leader_id, create_time, update_time) VALUES (2, 1, 'CTO', 5, '2024-03-13 22:48:01', '2024-03-16 19:41:07');
INSERT INTO test.department (id, parent_department_id, name, leader_id, create_time, update_time) VALUES (3, 2, '金融事业部', 3, '2024-03-13 22:48:01', '2024-03-16 12:41:54');
INSERT INTO test.department (id, parent_department_id, name, leader_id, create_time, update_time) VALUES (4, 2, '直播事业部', 4, '2024-03-16 19:32:22', '2024-03-16 19:41:22');
INSERT INTO test.department (id, parent_department_id, name, leader_id, create_time, update_time) VALUES (5, 1, '财务经管部', 3, '2024-03-16 19:32:50', '2024-03-16 19:41:30');
INSERT INTO test.department (id, parent_department_id, name, leader_id, create_time, update_time) VALUES (6, 1, '客户服务部', 7, '2024-03-16 19:33:56', '2024-03-16 19:43:45');
INSERT INTO test.department (id, parent_department_id, name, leader_id, create_time, update_time) VALUES (7, 1, '人力资源部', 2, '2024-03-17 01:17:45', '2024-03-17 01:17:46');

INSERT INTO test.person (id, username, name_zh, password, department_id, create_time, update_time) VALUES (1, 'zhangsan', '张三', '123456', 3, '2024-03-13 22:48:27', '2024-03-13 22:48:27');
INSERT INTO test.person (id, username, name_zh, password, department_id, create_time, update_time) VALUES (2, 'ganzhengguo', '甘正国', '123456', 3, '2024-03-13 22:48:27', '2024-03-15 22:55:08');
INSERT INTO test.person (id, username, name_zh, password, department_id, create_time, update_time) VALUES (3, 'xuyouyou', '许优优', '123456', 3, '2024-03-16 12:42:00', '2024-03-17 00:42:49');
INSERT INTO test.person (id, username, name_zh, password, department_id, create_time, update_time) VALUES (4, 'allen', 'Allen', '123456', 3, '2024-03-16 19:39:05', '2024-03-16 19:39:06');
INSERT INTO test.person (id, username, name_zh, password, department_id, create_time, update_time) VALUES (5, 'Bob', 'Bob', '123456', 2, '2024-03-15 19:39:33', '2024-03-16 19:39:57');
INSERT INTO test.person (id, username, name_zh, password, department_id, create_time, update_time) VALUES (6, 'MaJack', '杰克马', '123456', 1, '2024-03-16 19:40:38', '2024-03-16 19:40:38');
INSERT INTO test.person (id, username, name_zh, password, department_id, create_time, update_time) VALUES (7, 'liwenqinag', '李文强', '123456', 6, '2024-03-16 19:43:26', '2024-03-16 19:43:32');


INSERT INTO test.bank_card (id, owner_id, card_number, bank, area, create_time, update_time) VALUES (1, 1, '6228211999005912370', '招商银行', '北京支行', '2024-02-13 22:47:28', '2024-02-13 22:47:38');
INSERT INTO test.bank_card (id, owner_id, card_number, bank, area, create_time, update_time) VALUES (2, 3, '6228211999005912377', '招商银行', '安徽合肥支行', '2024-03-16 15:28:18', '2024-03-16 15:36:51');
INSERT INTO test.bank_card (id, owner_id, card_number, bank, area, create_time, update_time) VALUES (5, 2, '6228211999005912379', '招商银行', '北京支行', '2024-02-13 22:47:28', '2024-02-13 22:47:38');
INSERT INTO test.bank_card (id, owner_id, card_number, bank, area, create_time, update_time) VALUES (6, 3, '6228211999005912470', '招商银行', '北京支行', '2024-02-13 22:47:28', '2024-02-13 22:47:38');
INSERT INTO test.bank_card (id, owner_id, card_number, bank, area, create_time, update_time) VALUES (7, 4, '6228211999005912471', '招商银行', '北京支行', '2024-02-13 22:47:28', '2024-02-13 22:47:38');
INSERT INTO test.bank_card (id, owner_id, card_number, bank, area, create_time, update_time) VALUES (8, 5, '6228211999005912472', '招商银行', '北京支行', '2024-02-13 22:47:28', '2024-02-13 22:47:38');
INSERT INTO test.bank_card (id, owner_id, card_number, bank, area, create_time, update_time) VALUES (9, 6, '6228211999005912473', '招商银行', '北京支行', '2024-02-13 22:47:28', '2024-02-13 22:47:38');
INSERT INTO test.bank_card (id, owner_id, card_number, bank, area, create_time, update_time) VALUES (10, 7, '6228211999005912474', '招商银行', '北京支行', '2024-02-13 22:47:28', '2024-02-13 22:47:38');

INSERT INTO test.permission (id, uid, role, create_time, update_time) VALUES (1, 1, 'admin', '2024-03-16 23:14:48', '2024-03-16 23:14:48');


INSERT INTO test.process_def (id, process_name, process_key, recipe_type, process_xml, publish_process_xml, submit_uid, update_uid, status, process_definition_key, process_definition_id, variables, publish_variables, create_time, update_time) VALUES (1, '领导审批', 'leader', 'travelApply', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" xmlns:activiti="http://activiti.org/bpmn" id="sid-38422fae-e03e-43a3-bef4-bd33b32041b2" targetNamespace="http://bpmn.io/bpmn" exporter="bpmn-js (https://demo.bpmn.io)" exporterVersion="5.1.2"><process id="leader_approval_process" name="一级领导审批" isExecutable="true"><startEvent id="StartEvent_1y45yut" name="开始"><outgoing>SequenceFlow_0h21x7r</outgoing></startEvent><sequenceFlow id="SequenceFlow_0h21x7r" sourceRef="StartEvent_1y45yut" targetRef="submitterTask" /><sequenceFlow id="Flow_0nxozmh" sourceRef="submitterTask" targetRef="approverTask" /><sequenceFlow id="Flow_066znxb" sourceRef="approverTask" targetRef="Event_0mar1zt" /><userTask id="submitterTask" name="提交审批" activiti:assignee="${submitter}"><incoming>SequenceFlow_0h21x7r</incoming><outgoing>Flow_0nxozmh</outgoing></userTask><userTask id="approverTask" name="领导审批" activiti:assignee="${leader}"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0nxozmh</incoming><outgoing>Flow_066znxb</outgoing></userTask><endEvent id="Event_0mar1zt"><incoming>Flow_066znxb</incoming></endEvent></process><bpmndi:BPMNDiagram id="BpmnDiagram_1"><bpmndi:BPMNPlane id="BpmnPlane_1" bpmnElement="leader_approval_process"><bpmndi:BPMNEdge id="Flow_066znxb_di" bpmnElement="Flow_066znxb"><omgdi:waypoint x="500" y="120" /><omgdi:waypoint x="562" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0nxozmh_di" bpmnElement="Flow_0nxozmh"><omgdi:waypoint x="340" y="120" /><omgdi:waypoint x="400" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="SequenceFlow_0h21x7r_di" bpmnElement="SequenceFlow_0h21x7r"><omgdi:waypoint x="188" y="120" /><omgdi:waypoint x="240" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNShape id="StartEvent_1y45yut_di" bpmnElement="StartEvent_1y45yut"><omgdc:Bounds x="152" y="102" width="36" height="36" /><bpmndi:BPMNLabel><omgdc:Bounds x="160" y="145" width="22" height="14" /></bpmndi:BPMNLabel></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_10vr3xg_di" bpmnElement="submitterTask"><omgdc:Bounds x="240" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1w5h70x_di" bpmnElement="approverTask"><omgdc:Bounds x="400" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Event_0mar1zt_di" bpmnElement="Event_0mar1zt"><omgdc:Bounds x="562" y="102" width="36" height="36" /></bpmndi:BPMNShape></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" xmlns:activiti="http://activiti.org/bpmn" id="sid-38422fae-e03e-43a3-bef4-bd33b32041b2" targetNamespace="http://bpmn.io/bpmn" exporter="bpmn-js (https://demo.bpmn.io)" exporterVersion="5.1.2"><process id="leader_approval_process" name="一级领导审批" isExecutable="true"><startEvent id="StartEvent_1y45yut" name="开始"><outgoing>SequenceFlow_0h21x7r</outgoing></startEvent><sequenceFlow id="SequenceFlow_0h21x7r" sourceRef="StartEvent_1y45yut" targetRef="submitterTask" /><sequenceFlow id="Flow_0nxozmh" sourceRef="submitterTask" targetRef="approverTask" /><sequenceFlow id="Flow_066znxb" sourceRef="approverTask" targetRef="Event_0mar1zt" /><userTask id="submitterTask" name="提交审批" activiti:assignee="${submitter}"><incoming>SequenceFlow_0h21x7r</incoming><outgoing>Flow_0nxozmh</outgoing></userTask><userTask id="approverTask" name="领导审批" activiti:assignee="${leader}"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0nxozmh</incoming><outgoing>Flow_066znxb</outgoing></userTask><endEvent id="Event_0mar1zt"><incoming>Flow_066znxb</incoming></endEvent></process><bpmndi:BPMNDiagram id="BpmnDiagram_1"><bpmndi:BPMNPlane id="BpmnPlane_1" bpmnElement="leader_approval_process"><bpmndi:BPMNEdge id="Flow_066znxb_di" bpmnElement="Flow_066znxb"><omgdi:waypoint x="500" y="120" /><omgdi:waypoint x="562" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0nxozmh_di" bpmnElement="Flow_0nxozmh"><omgdi:waypoint x="340" y="120" /><omgdi:waypoint x="400" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="SequenceFlow_0h21x7r_di" bpmnElement="SequenceFlow_0h21x7r"><omgdi:waypoint x="188" y="120" /><omgdi:waypoint x="240" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNShape id="StartEvent_1y45yut_di" bpmnElement="StartEvent_1y45yut"><omgdc:Bounds x="152" y="102" width="36" height="36" /><bpmndi:BPMNLabel><omgdc:Bounds x="160" y="145" width="22" height="14" /></bpmndi:BPMNLabel></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_10vr3xg_di" bpmnElement="submitterTask"><omgdc:Bounds x="240" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1w5h70x_di" bpmnElement="approverTask"><omgdc:Bounds x="400" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Event_0mar1zt_di" bpmnElement="Event_0mar1zt"><omgdc:Bounds x="562" y="102" width="36" height="36" /></bpmndi:BPMNShape></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>', 1, 1, 0, 'leader_approval_process', 'leader_approval_process:1:19f42a1c-e3bd-11ee-8d17-326e85e1f7ed', '[{"taskId":"submitterTask","variable":"submitter"},{"taskId":"approverTask","variable":"leader"}]', '[{"taskId":"submitterTask","variable":"submitter"},{"taskId":"approverTask","variable":"leader"}]', '2024-03-14 23:38:26', '2024-03-17 01:50:21');
INSERT INTO test.process_def (id, process_name, process_key, recipe_type, process_xml, publish_process_xml, submit_uid, update_uid, status, process_definition_key, process_definition_id, variables, publish_variables, create_time, update_time) VALUES (2, '领导和财务审批', 'leader_and_finance', 'travelReimbursement', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" xmlns:activiti="http://activiti.org/bpmn" id="sid-38422fae-e03e-43a3-bef4-bd33b32041b2" targetNamespace="http://bpmn.io/bpmn" exporter="bpmn-js (https://demo.bpmn.io)" exporterVersion="5.1.2"><process id="leader_and_finance" name="领导和财务审批" isExecutable="true"><startEvent id="StartEvent_1y45yut" name="开始"><outgoing>SequenceFlow_0h21x7r</outgoing></startEvent><sequenceFlow id="SequenceFlow_0h21x7r" sourceRef="StartEvent_1y45yut" targetRef="submitterTask" /><sequenceFlow id="Flow_0mjdel7" sourceRef="submitterTask" targetRef="leaderApprovalTask" /><sequenceFlow id="Flow_0x5ppa9" sourceRef="leaderApprovalTask" targetRef="financeApprovalTask" /><sequenceFlow id="Flow_1ndc0ha" sourceRef="financeApprovalTask" targetRef="Event_0rb2ksk" /><userTask id="submitterTask" name="提交审批" activiti:assignee="${submitter}"><incoming>SequenceFlow_0h21x7r</incoming><outgoing>Flow_0mjdel7</outgoing></userTask><userTask id="leaderApprovalTask" name="领导审批" activiti:assignee="${leader}"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0mjdel7</incoming><outgoing>Flow_0x5ppa9</outgoing></userTask><userTask id="financeApprovalTask" name="财务审批" activiti:assignee="xuyouyou"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0x5ppa9</incoming><outgoing>Flow_1ndc0ha</outgoing></userTask><endEvent id="Event_0rb2ksk"><incoming>Flow_1ndc0ha</incoming></endEvent></process><bpmndi:BPMNDiagram id="BpmnDiagram_1"><bpmndi:BPMNPlane id="BpmnPlane_1" bpmnElement="leader_and_finance"><bpmndi:BPMNEdge id="Flow_1ndc0ha_di" bpmnElement="Flow_1ndc0ha"><omgdi:waypoint x="660" y="120" /><omgdi:waypoint x="722" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0x5ppa9_di" bpmnElement="Flow_0x5ppa9"><omgdi:waypoint x="500" y="120" /><omgdi:waypoint x="560" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0mjdel7_di" bpmnElement="Flow_0mjdel7"><omgdi:waypoint x="340" y="120" /><omgdi:waypoint x="400" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="SequenceFlow_0h21x7r_di" bpmnElement="SequenceFlow_0h21x7r"><omgdi:waypoint x="188" y="120" /><omgdi:waypoint x="240" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNShape id="StartEvent_1y45yut_di" bpmnElement="StartEvent_1y45yut"><omgdc:Bounds x="152" y="102" width="36" height="36" /><bpmndi:BPMNLabel><omgdc:Bounds x="160" y="145" width="22" height="14" /></bpmndi:BPMNLabel></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1dblf76_di" bpmnElement="submitterTask"><omgdc:Bounds x="240" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_0402az0_di" bpmnElement="leaderApprovalTask"><omgdc:Bounds x="400" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1r6t5fg_di" bpmnElement="financeApprovalTask"><omgdc:Bounds x="560" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Event_0rb2ksk_di" bpmnElement="Event_0rb2ksk"><omgdc:Bounds x="722" y="102" width="36" height="36" /></bpmndi:BPMNShape></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" xmlns:activiti="http://activiti.org/bpmn" id="sid-38422fae-e03e-43a3-bef4-bd33b32041b2" targetNamespace="http://bpmn.io/bpmn" exporter="bpmn-js (https://demo.bpmn.io)" exporterVersion="5.1.2"><process id="leader_and_finance" name="领导和财务审批" isExecutable="true"><startEvent id="StartEvent_1y45yut" name="开始"><outgoing>SequenceFlow_0h21x7r</outgoing></startEvent><sequenceFlow id="SequenceFlow_0h21x7r" sourceRef="StartEvent_1y45yut" targetRef="submitterTask" /><sequenceFlow id="Flow_0mjdel7" sourceRef="submitterTask" targetRef="leaderApprovalTask" /><sequenceFlow id="Flow_0x5ppa9" sourceRef="leaderApprovalTask" targetRef="financeApprovalTask" /><sequenceFlow id="Flow_1ndc0ha" sourceRef="financeApprovalTask" targetRef="Event_0rb2ksk" /><userTask id="submitterTask" name="提交审批" activiti:assignee="${submitter}"><incoming>SequenceFlow_0h21x7r</incoming><outgoing>Flow_0mjdel7</outgoing></userTask><userTask id="leaderApprovalTask" name="领导审批" activiti:assignee="${leader}"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0mjdel7</incoming><outgoing>Flow_0x5ppa9</outgoing></userTask><userTask id="financeApprovalTask" name="财务审批" activiti:assignee="xuyouyou"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0x5ppa9</incoming><outgoing>Flow_1ndc0ha</outgoing></userTask><endEvent id="Event_0rb2ksk"><incoming>Flow_1ndc0ha</incoming></endEvent></process><bpmndi:BPMNDiagram id="BpmnDiagram_1"><bpmndi:BPMNPlane id="BpmnPlane_1" bpmnElement="leader_and_finance"><bpmndi:BPMNEdge id="Flow_1ndc0ha_di" bpmnElement="Flow_1ndc0ha"><omgdi:waypoint x="660" y="120" /><omgdi:waypoint x="722" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0x5ppa9_di" bpmnElement="Flow_0x5ppa9"><omgdi:waypoint x="500" y="120" /><omgdi:waypoint x="560" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0mjdel7_di" bpmnElement="Flow_0mjdel7"><omgdi:waypoint x="340" y="120" /><omgdi:waypoint x="400" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="SequenceFlow_0h21x7r_di" bpmnElement="SequenceFlow_0h21x7r"><omgdi:waypoint x="188" y="120" /><omgdi:waypoint x="240" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNShape id="StartEvent_1y45yut_di" bpmnElement="StartEvent_1y45yut"><omgdc:Bounds x="152" y="102" width="36" height="36" /><bpmndi:BPMNLabel><omgdc:Bounds x="160" y="145" width="22" height="14" /></bpmndi:BPMNLabel></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1dblf76_di" bpmnElement="submitterTask"><omgdc:Bounds x="240" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_0402az0_di" bpmnElement="leaderApprovalTask"><omgdc:Bounds x="400" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1r6t5fg_di" bpmnElement="financeApprovalTask"><omgdc:Bounds x="560" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Event_0rb2ksk_di" bpmnElement="Event_0rb2ksk"><omgdc:Bounds x="722" y="102" width="36" height="36" /></bpmndi:BPMNShape></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>', 3, 1, 0, 'leader_and_finance', 'leader_and_finance:1:bd5aa2cd-e3ba-11ee-ab52-326e85e1f7ed', '[{"taskId":"submitterTask","variable":"submitter"},{"taskId":"leaderApprovalTask","variable":"leader"}]', '[{"taskId":"submitterTask","variable":"submitter"},{"taskId":"leaderApprovalTask","variable":"leader"}]', '2024-03-17 01:51:01', '2024-03-17 01:51:09');
INSERT INTO test.process_def (id, process_name, process_key, recipe_type, process_xml, publish_process_xml, submit_uid, update_uid, status, process_definition_key, process_definition_id, variables, publish_variables, create_time, update_time) VALUES (3, '领导和HR审批', 'leader_and_hr', 'overtimeApply', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" xmlns:activiti="http://activiti.org/bpmn" id="sid-38422fae-e03e-43a3-bef4-bd33b32041b2" targetNamespace="http://bpmn.io/bpmn" exporter="bpmn-js (https://demo.bpmn.io)" exporterVersion="5.1.2"><process id="leader_and_hr" name="领导和HR审批" isExecutable="true"><startEvent id="StartEvent_1y45yut" name="开始"><outgoing>SequenceFlow_0h21x7r</outgoing></startEvent><sequenceFlow id="SequenceFlow_0h21x7r" sourceRef="StartEvent_1y45yut" targetRef="submitterTask" /><sequenceFlow id="Flow_0mjdel7" sourceRef="submitterTask" targetRef="leaderApprovalTask" /><sequenceFlow id="Flow_0x5ppa9" sourceRef="leaderApprovalTask" targetRef="hrApprovalTask" /><sequenceFlow id="Flow_1ndc0ha" sourceRef="hrApprovalTask" targetRef="Event_0rb2ksk" /><userTask id="submitterTask" name="提交审批" activiti:assignee="${submitter}"><incoming>SequenceFlow_0h21x7r</incoming><outgoing>Flow_0mjdel7</outgoing></userTask><userTask id="leaderApprovalTask" name="领导审批" activiti:assignee="${leader}"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0mjdel7</incoming><outgoing>Flow_0x5ppa9</outgoing></userTask><userTask id="hrApprovalTask" name="HR审批" activiti:assignee="ganzhengguo"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0x5ppa9</incoming><outgoing>Flow_1ndc0ha</outgoing></userTask><endEvent id="Event_0rb2ksk"><incoming>Flow_1ndc0ha</incoming></endEvent></process><bpmndi:BPMNDiagram id="BpmnDiagram_1"><bpmndi:BPMNPlane id="BpmnPlane_1" bpmnElement="leader_and_hr"><bpmndi:BPMNEdge id="Flow_1ndc0ha_di" bpmnElement="Flow_1ndc0ha"><omgdi:waypoint x="660" y="120" /><omgdi:waypoint x="722" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0x5ppa9_di" bpmnElement="Flow_0x5ppa9"><omgdi:waypoint x="500" y="120" /><omgdi:waypoint x="560" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0mjdel7_di" bpmnElement="Flow_0mjdel7"><omgdi:waypoint x="340" y="120" /><omgdi:waypoint x="400" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="SequenceFlow_0h21x7r_di" bpmnElement="SequenceFlow_0h21x7r"><omgdi:waypoint x="188" y="120" /><omgdi:waypoint x="240" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNShape id="StartEvent_1y45yut_di" bpmnElement="StartEvent_1y45yut"><omgdc:Bounds x="152" y="102" width="36" height="36" /><bpmndi:BPMNLabel><omgdc:Bounds x="160" y="145" width="22" height="14" /></bpmndi:BPMNLabel></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1dblf76_di" bpmnElement="submitterTask"><omgdc:Bounds x="240" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_0402az0_di" bpmnElement="leaderApprovalTask"><omgdc:Bounds x="400" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1r6t5fg_di" bpmnElement="hrApprovalTask"><omgdc:Bounds x="560" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Event_0rb2ksk_di" bpmnElement="Event_0rb2ksk"><omgdc:Bounds x="722" y="102" width="36" height="36" /></bpmndi:BPMNShape></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" xmlns:activiti="http://activiti.org/bpmn" id="sid-38422fae-e03e-43a3-bef4-bd33b32041b2" targetNamespace="http://bpmn.io/bpmn" exporter="bpmn-js (https://demo.bpmn.io)" exporterVersion="5.1.2"><process id="leader_and_hr" name="领导和HR审批" isExecutable="true"><startEvent id="StartEvent_1y45yut" name="开始"><outgoing>SequenceFlow_0h21x7r</outgoing></startEvent><sequenceFlow id="SequenceFlow_0h21x7r" sourceRef="StartEvent_1y45yut" targetRef="submitterTask" /><sequenceFlow id="Flow_0mjdel7" sourceRef="submitterTask" targetRef="leaderApprovalTask" /><sequenceFlow id="Flow_0x5ppa9" sourceRef="leaderApprovalTask" targetRef="hrApprovalTask" /><sequenceFlow id="Flow_1ndc0ha" sourceRef="hrApprovalTask" targetRef="Event_0rb2ksk" /><userTask id="submitterTask" name="提交审批" activiti:assignee="${submitter}"><incoming>SequenceFlow_0h21x7r</incoming><outgoing>Flow_0mjdel7</outgoing></userTask><userTask id="leaderApprovalTask" name="领导审批" activiti:assignee="${leader}"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0mjdel7</incoming><outgoing>Flow_0x5ppa9</outgoing></userTask><userTask id="hrApprovalTask" name="HR审批" activiti:assignee="ganzhengguo"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0x5ppa9</incoming><outgoing>Flow_1ndc0ha</outgoing></userTask><endEvent id="Event_0rb2ksk"><incoming>Flow_1ndc0ha</incoming></endEvent></process><bpmndi:BPMNDiagram id="BpmnDiagram_1"><bpmndi:BPMNPlane id="BpmnPlane_1" bpmnElement="leader_and_hr"><bpmndi:BPMNEdge id="Flow_1ndc0ha_di" bpmnElement="Flow_1ndc0ha"><omgdi:waypoint x="660" y="120" /><omgdi:waypoint x="722" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0x5ppa9_di" bpmnElement="Flow_0x5ppa9"><omgdi:waypoint x="500" y="120" /><omgdi:waypoint x="560" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0mjdel7_di" bpmnElement="Flow_0mjdel7"><omgdi:waypoint x="340" y="120" /><omgdi:waypoint x="400" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="SequenceFlow_0h21x7r_di" bpmnElement="SequenceFlow_0h21x7r"><omgdi:waypoint x="188" y="120" /><omgdi:waypoint x="240" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNShape id="StartEvent_1y45yut_di" bpmnElement="StartEvent_1y45yut"><omgdc:Bounds x="152" y="102" width="36" height="36" /><bpmndi:BPMNLabel><omgdc:Bounds x="160" y="145" width="22" height="14" /></bpmndi:BPMNLabel></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1dblf76_di" bpmnElement="submitterTask"><omgdc:Bounds x="240" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_0402az0_di" bpmnElement="leaderApprovalTask"><omgdc:Bounds x="400" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1r6t5fg_di" bpmnElement="hrApprovalTask"><omgdc:Bounds x="560" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Event_0rb2ksk_di" bpmnElement="Event_0rb2ksk"><omgdc:Bounds x="722" y="102" width="36" height="36" /></bpmndi:BPMNShape></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>', 2, 1, 0, 'leader_and_hr', 'leader_and_hr:1:be195ef0-e3ba-11ee-ab52-326e85e1f7ed', '[{"taskId":"submitterTask","variable":"submitter"},{"taskId":"leaderApprovalTask","variable":"leader"}]', '[{"taskId":"submitterTask","variable":"submitter"},{"taskId":"leaderApprovalTask","variable":"leader"}]', '2024-03-17 01:19:06', '2024-03-17 01:51:26');
INSERT INTO test.process_def (id, process_name, process_key, recipe_type, process_xml, publish_process_xml, submit_uid, update_uid, status, process_definition_key, process_definition_id, variables, publish_variables, create_time, update_time) VALUES (4, 'HR审批', 'hr', 'dailyReimbursement', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" xmlns:activiti="http://activiti.org/bpmn" id="sid-38422fae-e03e-43a3-bef4-bd33b32041b2" targetNamespace="http://bpmn.io/bpmn" exporter="bpmn-js (https://demo.bpmn.io)" exporterVersion="5.1.2"><process id="hr_approval_process" name="一级HR审批" isExecutable="true"><startEvent id="StartEvent_1y45yut" name="开始"><outgoing>SequenceFlow_0h21x7r</outgoing></startEvent><sequenceFlow id="SequenceFlow_0h21x7r" sourceRef="StartEvent_1y45yut" targetRef="submitterTask" /><sequenceFlow id="Flow_0nxozmh" sourceRef="submitterTask" targetRef="hrApproverTask" /><sequenceFlow id="Flow_066znxb" sourceRef="hrApproverTask" targetRef="Event_0mar1zt" /><userTask id="submitterTask" name="提交审批" activiti:assignee="${submitter}"><incoming>SequenceFlow_0h21x7r</incoming><outgoing>Flow_0nxozmh</outgoing></userTask><userTask id="hrApproverTask" name="HR审批" activiti:assignee="ganzhengguo"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0nxozmh</incoming><outgoing>Flow_066znxb</outgoing></userTask><endEvent id="Event_0mar1zt"><incoming>Flow_066znxb</incoming></endEvent></process><bpmndi:BPMNDiagram id="BpmnDiagram_1"><bpmndi:BPMNPlane id="BpmnPlane_1" bpmnElement="hr_approval_process"><bpmndi:BPMNEdge id="Flow_066znxb_di" bpmnElement="Flow_066znxb"><omgdi:waypoint x="500" y="120" /><omgdi:waypoint x="562" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0nxozmh_di" bpmnElement="Flow_0nxozmh"><omgdi:waypoint x="340" y="120" /><omgdi:waypoint x="400" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="SequenceFlow_0h21x7r_di" bpmnElement="SequenceFlow_0h21x7r"><omgdi:waypoint x="188" y="120" /><omgdi:waypoint x="240" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNShape id="StartEvent_1y45yut_di" bpmnElement="StartEvent_1y45yut"><omgdc:Bounds x="152" y="102" width="36" height="36" /><bpmndi:BPMNLabel><omgdc:Bounds x="160" y="145" width="22" height="14" /></bpmndi:BPMNLabel></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_10vr3xg_di" bpmnElement="submitterTask"><omgdc:Bounds x="240" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1w5h70x_di" bpmnElement="hrApproverTask"><omgdc:Bounds x="400" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Event_0mar1zt_di" bpmnElement="Event_0mar1zt"><omgdc:Bounds x="562" y="102" width="36" height="36" /></bpmndi:BPMNShape></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" xmlns:activiti="http://activiti.org/bpmn" id="sid-38422fae-e03e-43a3-bef4-bd33b32041b2" targetNamespace="http://bpmn.io/bpmn" exporter="bpmn-js (https://demo.bpmn.io)" exporterVersion="5.1.2"><process id="hr_approval_process" name="一级HR审批" isExecutable="true"><startEvent id="StartEvent_1y45yut" name="开始"><outgoing>SequenceFlow_0h21x7r</outgoing></startEvent><sequenceFlow id="SequenceFlow_0h21x7r" sourceRef="StartEvent_1y45yut" targetRef="submitterTask" /><sequenceFlow id="Flow_0nxozmh" sourceRef="submitterTask" targetRef="hrApproverTask" /><sequenceFlow id="Flow_066znxb" sourceRef="hrApproverTask" targetRef="Event_0mar1zt" /><userTask id="submitterTask" name="提交审批" activiti:assignee="${submitter}"><incoming>SequenceFlow_0h21x7r</incoming><outgoing>Flow_0nxozmh</outgoing></userTask><userTask id="hrApproverTask" name="HR审批" activiti:assignee="ganzhengguo"><extensionElements><activiti:formProperty id="approvalResult" label="approvalResult" type="boolean" defaultValue="false" name="approvalResult" default="false" /></extensionElements><incoming>Flow_0nxozmh</incoming><outgoing>Flow_066znxb</outgoing></userTask><endEvent id="Event_0mar1zt"><incoming>Flow_066znxb</incoming></endEvent></process><bpmndi:BPMNDiagram id="BpmnDiagram_1"><bpmndi:BPMNPlane id="BpmnPlane_1" bpmnElement="hr_approval_process"><bpmndi:BPMNEdge id="Flow_066znxb_di" bpmnElement="Flow_066znxb"><omgdi:waypoint x="500" y="120" /><omgdi:waypoint x="562" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="Flow_0nxozmh_di" bpmnElement="Flow_0nxozmh"><omgdi:waypoint x="340" y="120" /><omgdi:waypoint x="400" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNEdge id="SequenceFlow_0h21x7r_di" bpmnElement="SequenceFlow_0h21x7r"><omgdi:waypoint x="188" y="120" /><omgdi:waypoint x="240" y="120" /></bpmndi:BPMNEdge><bpmndi:BPMNShape id="StartEvent_1y45yut_di" bpmnElement="StartEvent_1y45yut"><omgdc:Bounds x="152" y="102" width="36" height="36" /><bpmndi:BPMNLabel><omgdc:Bounds x="160" y="145" width="22" height="14" /></bpmndi:BPMNLabel></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_10vr3xg_di" bpmnElement="submitterTask"><omgdc:Bounds x="240" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Activity_1w5h70x_di" bpmnElement="hrApproverTask"><omgdc:Bounds x="400" y="80" width="100" height="80" /></bpmndi:BPMNShape><bpmndi:BPMNShape id="Event_0mar1zt_di" bpmnElement="Event_0mar1zt"><omgdc:Bounds x="562" y="102" width="36" height="36" /></bpmndi:BPMNShape></bpmndi:BPMNPlane></bpmndi:BPMNDiagram></definitions>', 3, 1, 0, 'hr_approval_process', 'hr_approval_process:1:1dd434f3-e3bb-11ee-ab52-326e85e1f7ed', '[{"taskId":"submitterTask","variable":"submitter"}]', '[{"taskId":"submitterTask","variable":"submitter"}]', '2024-03-17 01:30:15', '2024-03-17 01:51:40');