--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_ZXKF_ISSUES:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_ZXKF_ISSUES
(
  issues_id      VARCHAR2(32) not null,
  title          VARCHAR2(500),
  issues_content BLOB,
  status         VARCHAR2(2) default 0,
  update_time    DATE,
  create_time    DATE,
  is_open        VARCHAR2(2),
  user_id        VARCHAR2(32)
);

--changeset DCHY_XMGL_ZXKF_ISSUES:2 failOnError:false runOnChange:true runAlways:false
comment on table DCHY_XMGL_ZXKF_ISSUES
  is '在线客服_提问表';
--changeset DCHY_XMGL_ZXKF_ISSUES:3 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ISSUES.issues_id
  is '主键';
  --changeset DCHY_XMGL_ZXKF_ISSUES:4 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ISSUES.title
  is '标题';
  --changeset DCHY_XMGL_ZXKF_ISSUES:5 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ISSUES.issues_content
  is '内容';
  --changeset DCHY_XMGL_ZXKF_ISSUES:6 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ISSUES.status
  is '是否回复';
  --changeset DCHY_XMGL_ZXKF_ISSUES:7 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ISSUES.update_time
  is '更新时间';
  --changeset DCHY_XMGL_ZXKF_ISSUES:8 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ISSUES.create_time
  is '创建时间';
  --changeset DCHY_XMGL_ZXKF_ISSUES:9 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ISSUES.is_open
  is '是否公开';
  --changeset DCHY_XMGL_ZXKF_ISSUES:10 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZXKF_ISSUES.user_id
  is '提问人编号';
--changeset DCHY_XMGL_ZXKF_ISSUES:11 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_ZXKF_ISSUES
  add primary key (ISSUES_ID);

  --changeset DCHY_XMGL_ZXKF_ISSUES:12 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_ZXKF_ISSUES add sfyx VARCHAR2(2);
comment on column DCHY_XMGL_ZXKF_ISSUES.sfyx is '是否有效';
