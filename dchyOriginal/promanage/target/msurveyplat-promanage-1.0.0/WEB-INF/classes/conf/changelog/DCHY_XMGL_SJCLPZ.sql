--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_SJCLPZ:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_SJCLPZ add need INTEGER default 0 not null;
--changeset DCHY_XMGL_SJCLPZ:2 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_SJCLPZ.need is '是否必传';