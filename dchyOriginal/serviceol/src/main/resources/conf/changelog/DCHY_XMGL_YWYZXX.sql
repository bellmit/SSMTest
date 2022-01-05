--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_YWYZXX:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_YWYZXX add ywms VARCHAR2(50);
alter table DCHY_XMGL_YWYZXX add sfqy VARCHAR2(1);
comment on column DCHY_XMGL_YWYZXX.ywms is '业务描述';
comment on column DCHY_XMGL_YWYZXX.sfqy is '是否启用(字典表A.10)';