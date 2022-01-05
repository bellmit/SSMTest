--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_CHXM_CHDWXX:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CHXM_CHDWXX add pjzt VARCHAR2(10);
