--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_JCSJSQXX:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_JCSJSQXX
(
  jcsjsqxxid VARCHAR2(32) not null,
  sqsj       DATE,
  babh       VARCHAR2(32),
  gcbh       VARCHAR2(32),
  gcmc       VARCHAR2(32),
  jsdw       VARCHAR2(500),
  sqfs       VARCHAR2(2),
  dqzt       VARCHAR2(2),
  chxmid     VARCHAR2(32),
  chdwmc     VARCHAR2(500),
  sqr        VARCHAR2(32),
  sqrid      VARCHAR2(32),
  zzr        VARCHAR2(32),
  zzrid      VARCHAR2(32),
  shr        VARCHAR2(32),
  shrid      VARCHAR2(32),
  zzsj       DATE,
  shsj       DATE
);

-- Add comments to the table 
comment on table DCHY_XMGL_JCSJSQXX
  is '多测合一基础数据申请信息';
-- Add comments to the columns 
comment on column DCHY_XMGL_JCSJSQXX.jcsjsqxxid
  is '申请信息id';
comment on column DCHY_XMGL_JCSJSQXX.sqsj
  is '申请时间';
comment on column DCHY_XMGL_JCSJSQXX.babh
  is '备案编号';
comment on column DCHY_XMGL_JCSJSQXX.gcbh
  is '工程编号';
comment on column DCHY_XMGL_JCSJSQXX.gcmc
  is '工程名称';
comment on column DCHY_XMGL_JCSJSQXX.jsdw
  is '建设单位';
comment on column DCHY_XMGL_JCSJSQXX.sqfs
  is '申请方式(项目来源)';
comment on column DCHY_XMGL_JCSJSQXX.dqzt
  is '当前状态(1:审核中,2:待制作,3:制作中,4:待交付,98:已交付,99:已退回)';
comment on column DCHY_XMGL_JCSJSQXX.chxmid
  is '测绘项目id';
comment on column DCHY_XMGL_JCSJSQXX.chdwmc
  is '测绘单位名称';
comment on column DCHY_XMGL_JCSJSQXX.sqr
  is '申请人';
comment on column DCHY_XMGL_JCSJSQXX.sqrid
  is '申请人id';
comment on column DCHY_XMGL_JCSJSQXX.zzr
  is '制作人';
comment on column DCHY_XMGL_JCSJSQXX.zzrid
  is '制作人id';
comment on column DCHY_XMGL_JCSJSQXX.shr
  is '审核人';
comment on column DCHY_XMGL_JCSJSQXX.shrid
  is '审核人id';
comment on column DCHY_XMGL_JCSJSQXX.zzsj
  is '制作时间';
comment on column DCHY_XMGL_JCSJSQXX.shsj
  is '审核时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table DCHY_XMGL_JCSJSQXX
  add primary key (JCSJSQXXID)

  --changeset DCHY_XMGL_JCSJSQXX:2 failOnError:false runOnChange:true runAlways:false
  alter table DCHY_XMGL_JCSJSQXX add XMBABH VARCHAR2(32;

