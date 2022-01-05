--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_CHXM_GQXX:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_CHXM_GQXX
(
  gqxxid VARCHAR2(32) not null,
  chxmid VARCHAR2(32),
  gqzt   VARCHAR2(1),
  gqr VARCHAR2(32),
  gqsj   DATE,
  jgr VARCHAR2(32),
  jgsj   DATE
)
--changeset DCHY_XMGL_CHXM_GQXX:2 failOnError:false runOnChange:true runAlways:false
comment on table DCHY_XMGL_CHXM_GQXX is '多测合一项目挂起信息';
--changeset DCHY_XMGL_CHXM_GQXX:3 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CHXM_GQXX.gqxxid is '项目挂起信息id';
--changeset DCHY_XMGL_CHXM_GQXX:4 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CHXM_GQXX.chxmid is '测绘项目id';
--changeset DCHY_XMGL_CHXM_GQXX:5 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CHXM_GQXX.gqzt is '挂起状态(字典表A.10)';
--changeset DCHY_XMGL_CHXM_GQXX:6 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CHXM_GQXX.gqr is '挂起人';
--changeset DCHY_XMGL_CHXM_GQXX:7 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CHXM_GQXX.gqsj is '挂起时间';
--changeset DCHY_XMGL_CHXM_GQXX:8 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CHXM_GQXX.jgr is '解挂人';
--changeset DCHY_XMGL_CHXM_GQXX:9 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CHXM_GQXX.jgsj is '解挂时间';


