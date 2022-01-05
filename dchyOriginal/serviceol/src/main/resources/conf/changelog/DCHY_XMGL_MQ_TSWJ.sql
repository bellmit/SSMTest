--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_MQ_TSWJ:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_MQ_TSWJ
(
  tswjid VARCHAR2(32) not null,
  jssj   DATE,
  wjnr   BLOB,
  chxmid VARCHAR2(32),
  sjxxid VARCHAR2(32),
  sjclid VARCHAR2(32),
  htxxid VARCHAR2(32)
)
--changeset DCHY_XMGL_MQ_TSWJ:2 failOnError:false runOnChange:true runAlways:false-
comment on table DCHY_XMGL_MQ_TSWJ
  is '多测合一mq接收推送文件';
--changeset DCHY_XMGL_MQ_TSWJ:3 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_MQ_TSWJ.tswjid is '推送文件id';
--changeset DCHY_XMGL_MQ_TSWJ:4 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_MQ_TSWJ.jssj is '接收时间';
--changeset DCHY_XMGL_MQ_TSWJ:5 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_MQ_TSWJ.wjnr is '文件内容';
  --changeset DCHY_XMGL_MQ_TSWJ:6 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_MQ_TSWJ.chxmid is '测绘项目id';
  --changeset DCHY_XMGL_MQ_TSWJ:7 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_MQ_TSWJ.sjxxid is '收件信息id';
  --changeset DCHY_XMGL_MQ_TSWJ:8 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_MQ_TSWJ.sjclid is '收件材料id';
  --changeset DCHY_XMGL_MQ_TSWJ:9 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_MQ_TSWJ.htxxid is '合同信息id';

 --changeset DCHY_XMGL_MQ_TSWJ:10 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_MQ_TSWJ add xqfbbh VARCHAR2(32);
comment on column DCHY_XMGL_MQ_TSWJ.xqfbbh
  is '需求发布编号';
