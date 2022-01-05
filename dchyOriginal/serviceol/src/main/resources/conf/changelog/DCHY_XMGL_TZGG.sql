--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_TZGG:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_TZGG add sfyx VARCHAR2(2);
comment on column DCHY_XMGL_TZGG.sfyx is '是否有效';