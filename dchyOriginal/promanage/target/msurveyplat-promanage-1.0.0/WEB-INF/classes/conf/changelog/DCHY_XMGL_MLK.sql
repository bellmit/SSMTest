--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_MLK:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_MLK add MLKTP BLOB;
