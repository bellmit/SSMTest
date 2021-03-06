--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_SJCLPZ_SSCLSX_GX:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_SJCLPZ_SSCLSX_GX
(
    SJCLPZID VARCHAR2(32) not null,
    SSCLSXID VARCHAR2(32) not null
);
comment on column DCHY_XMGL_SJCLPZ_SSCLSX_GX.SJCLPZID is '收件材料配置编号';
comment on column DCHY_XMGL_SJCLPZ_SSCLSX_GX.SSCLSXID is '所属测量事项编号';


