--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_MQ_CZRZ:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_MQ_CZRZ add DCHY_XMGL_MQ_CZRZ.DLDM VARCHAR2(32);
alter table DCHY_XMGL_MQ_CZRZ add DCHY_XMGL_MQ_CZRZ.DLMC VARCHAR2(32);

--changeset DCHY_XMGL_MQ_CZRZ:2 failOnError:false runOnChange:true runAlways:false
alter table dchy_xmgl_mq_czrz modify(DCHY_XMGL_MQ_CZRZ.DLMC varchar(200));
