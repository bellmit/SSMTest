--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_ZXKF_ANSWER:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_ZXKF_ANSWER
(
  answer_id      VARCHAR2(32) not null,
  answer_content BLOB,
  issues_id      VARCHAR2(32),
  create_time    DATE,
  update_time    DATE,
  user_id        VARCHAR2(32)
);

--changeset DCHY_XMGL_ZXKF_ANSWER:2 failOnError:false runOnChange:true runAlways:false
comment on table DCHY_XMGL_ZXKF_ANSWER
  is '在线客服_回答';
--changeset DCHY_XMGL_ZXKF_ANSWER:3 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ANSWER.answer_id
  is '回答主键';
  --changeset DCHY_XMGL_ZXKF_ANSWER:4 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ANSWER.answer_content
  is '回答内容';
  --changeset DCHY_XMGL_ZXKF_ANSWER:5 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ANSWER.issues_id
  is '提问编号';
  --changeset DCHY_XMGL_ZXKF_ANSWER:6 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ANSWER.create_time
  is '创建时间';
  --changeset DCHY_XMGL_ZXKF_ANSWER:7 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ANSWER.update_time
  is '修改时间';
  --changeset DCHY_XMGL_ZXKF_ANSWER:8 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ANSWER.user_id
  is '回答人编号';
--changeset DCHY_XMGL_ZXKF_ANSWER:9 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_ZXKF_ANSWER
  add primary key (ANSWER_ID);

