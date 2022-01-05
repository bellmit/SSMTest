--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_AUTHORIZE:1 failOnError:false runOnChange:true runAlways:false
-- Create table
create table DCHY_XMGL_AUTHORIZE
(
  id     VARCHAR2(32) not null,
  sqlx   VARCHAR2(2),
  zylx   VARCHAR2(2),
  zyuri  VARCHAR2(100),
  roleid VARCHAR2(50)
);
--changeset DCHY_XMGL_AUTHORIZE:2 failOnError:false runOnChange:true runAlways:false
comment on table DCHY_XMGL_AUTHORIZE is '多测合一项目管理访问授权';
comment on column DCHY_XMGL_AUTHORIZE.id is '授权id';
comment on column DCHY_XMGL_AUTHORIZE.sqlx is '授权类型(字典项A.39)';
comment on column DCHY_XMGL_AUTHORIZE.zylx is '资源类型(字典项A.40)';
comment on column DCHY_XMGL_AUTHORIZE.zyuri is '资源uri标识';
comment on column DCHY_XMGL_AUTHORIZE.roleid is '角色id';