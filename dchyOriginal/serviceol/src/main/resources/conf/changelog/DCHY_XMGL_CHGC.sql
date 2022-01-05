--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_CHGC:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CHGC modify gcbh null;
--changeset DCHY_XMGL_CHGC:2 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CHGC add jsdwm VARCHAR2(32);
--changeset DCHY_XMGL_CHGC:3 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CHGC.jsdwm is '建设单位码';
