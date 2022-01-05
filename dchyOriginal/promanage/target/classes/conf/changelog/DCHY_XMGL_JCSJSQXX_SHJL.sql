--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_JCSJSQXX_SHJL:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_JCSJSQXX_SHJL
(
  shjlid     VARCHAR2(32) not null,
  jcsjsqxxid VARCHAR2(32),
  shsj       DATE,
  shr        VARCHAR2(32),
  shyj       VARCHAR2(200),
  shjg       VARCHAR2(22),
  shrid      VARCHAR2(32)
);
comment on table DCHY_XMGL_JCSJSQXX_SHJL
  is '多测合一基础数据申请审核记录';
comment on column DCHY_XMGL_JCSJSQXX_SHJL.shjlid
  is '审核记录id';
comment on column DCHY_XMGL_JCSJSQXX_SHJL.jcsjsqxxid
  is '基础数据申请信息id';
comment on column DCHY_XMGL_JCSJSQXX_SHJL.shsj
  is '审核时间';
comment on column DCHY_XMGL_JCSJSQXX_SHJL.shr
  is '审核人';
comment on column DCHY_XMGL_JCSJSQXX_SHJL.shyj
  is '审核意见';
comment on column DCHY_XMGL_JCSJSQXX_SHJL.shjg
  is '审核结果是否通过(字典项A.10)';
comment on column DCHY_XMGL_JCSJSQXX_SHJL.shrid
  is '审核人id';
alter table DCHY_XMGL_JCSJSQXX_SHJL add primary key (SHJLID);

