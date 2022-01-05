--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_SJCL:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_SJCL add clsx VARCHAR2(32);
--changeset DCHY_XMGL_SJCL:2 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_SJCL.clsx is '测量事项';
