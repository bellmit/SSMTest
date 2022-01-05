--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_MLK_CXJL:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_MLK_CXJL
(
  cxjlid VARCHAR2(32) not null,
  jlsj   DATE not null,
  jlrid  VARCHAR2(32) not null,
  jlrmc  VARCHAR2(32) not null,
  cxpj   VARCHAR2(2000),
  mlkid  VARCHAR2(32)
)
tablespace USERS
pctfree 10
initrans 1
maxtrans 255
storage
(
initial 64K
next 1M
minextents 1
maxextents unlimited
);
--changeset DCHY_XMGL_MLK_CXJL:2 failOnError:false runOnChange:true runAlways:false
comment on table DCHY_XMGL_MLK_CXJL
is '多测合一项目管理名录库诚信记录';
comment on column DCHY_XMGL_MLK_CXJL.cxjlid
is '诚信记录id';
comment on column DCHY_XMGL_MLK_CXJL.jlsj
is '记录时间';
comment on column DCHY_XMGL_MLK_CXJL.jlrid
is '记录人id';
comment on column DCHY_XMGL_MLK_CXJL.jlrmc
is '记录人名称';
comment on column DCHY_XMGL_MLK_CXJL.cxpj
is '诚信评价';
comment on column DCHY_XMGL_MLK_CXJL.mlkid
is '名录库id';
--changeset DCHY_XMGL_MLK_CXJL:3 failOnError:false runOnChange:true runAlways:false
create index IDX_DCHY_XMGL_MLK_CXJL_MLKID on DCHY_XMGL_MLK_CXJL (MLKID);
--changeset DCHY_XMGL_MLK_CXJL:4 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_MLK_CXJL add constraint PK_DCHY_XMGL_MLK_CXJL_ID primary key (CXJLID);

