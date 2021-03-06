--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_CGCC:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_JCSJSQXX_JDXX
(
  jdxxid     VARCHAR2(32) not null,
  jcsjsqxxid VARCHAR2(32),
  bjsj       DATE,
  bjry       VARCHAR2(32),
  bjryid     VARCHAR2(32),
  bjjg       VARCHAR2(32),
  bjyj       VARCHAR2(200),
  hj         VARCHAR2(10)
);
comment on column DCHY_XMGL_JCSJSQXX_JDXX.jdxxid is '进度信息id';
comment on column DCHY_XMGL_JCSJSQXX_JDXX.jcsjsqxxid is '基础数据申请信息id';
comment on column DCHY_XMGL_JCSJSQXX_JDXX.bjsj is '办结时间';
comment on column DCHY_XMGL_JCSJSQXX_JDXX.bjry is '办结人员';
comment on column DCHY_XMGL_JCSJSQXX_JDXX.bjryid is '办结人员id';
comment on column DCHY_XMGL_JCSJSQXX_JDXX.bjjg is '办结结果';
comment on column DCHY_XMGL_JCSJSQXX_JDXX.bjyj is '办结意见';
comment on column DCHY_XMGL_JCSJSQXX_JDXX.hj is '环节';
alter table DCHY_XMGL_JCSJSQXX_JDXX add primary key (JDXXID);

