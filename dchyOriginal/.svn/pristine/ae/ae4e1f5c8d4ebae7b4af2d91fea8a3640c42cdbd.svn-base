--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_CLSX_CHTL_PZ:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_CLSX_CHTL_PZ
(
  chtlpzid VARCHAR2(32) not null,
  clsx     VARCHAR2(50),
  jsgs     VARCHAR2(1000),
  clsxmc   VARCHAR2(1000),
  fdm      VARCHAR2(50)
)
--changeset DCHY_XMGL_CLSX_CHTL_PZ:2 failOnError:false runOnChange:true runAlways:false
comment on table DCHY_XMGL_CLSX_CHTL_PZ is '多测合一项目测绘体量配置信息';
--changeset DCHY_XMGL_CLSX_CHTL_PZ:3 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CLSX_CHTL_PZ.chtlpzid is '项目体量id';
--changeset DCHY_XMGL_CLSX_CHTL_PZ:4 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CLSX_CHTL_PZ.clsx is '测绘事项(字典表A.3)';
--changeset DCHY_XMGL_CLSX_CHTL_PZ:5 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CLSX_CHTL_PZ.jsgs is '计算公式';
--changeset DCHY_XMGL_CLSX_CHTL_PZ:6 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CLSX_CHTL_PZ.clsxmc is '测绘事项名称';
--changeset DCHY_XMGL_CLSX_CHTL_PZ:7 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CLSX_CHTL_PZ.fdm is '测绘事项父代码';








