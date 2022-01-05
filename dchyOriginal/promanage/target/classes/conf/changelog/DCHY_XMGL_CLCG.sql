--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_CLCG:1 failOnError:false runOnChange:true runAlways:false
-- Create table
create table DCHY_XMGL_CLCG
(
  clcgid VARCHAR2(32) not null,
  chxmid VARCHAR2(32),
  clsxid VARCHAR2(32),
  clsx   VARCHAR2(32),
  sqxxid VARCHAR2(32),
  shzt   VARCHAR2(2),
  chgcbh VARCHAR2(32),
  chgcid VARCHAR2(32),
  clcgmc VARCHAR2(100),
  sjclid VARCHAR2(32),
  sjxxid VARCHAR2(32),
  wjzxid VARCHAR2(32),
  rksj   DATE,
  tjsj   DATE,
  tjr    VARCHAR2(32),
  tjrmc  VARCHAR2(32)
);
-- Add comments to the table
comment on table DCHY_XMGL_CLCG
  is '多测合一项目管理测量成果';
-- Add comments to the columns
comment on column DCHY_XMGL_CLCG.clcgid
  is '测量成果id';
comment on column DCHY_XMGL_CLCG.chxmid
  is '测绘项目id';
comment on column DCHY_XMGL_CLCG.clsxid
  is '测量事项id';
comment on column DCHY_XMGL_CLCG.clsx
  is '测量事项(字典表A.3)';
comment on column DCHY_XMGL_CLCG.sqxxid
  is '申请信息id';
comment on column DCHY_XMGL_CLCG.shzt
  is '审核状态(字典表)';
comment on column DCHY_XMGL_CLCG.chgcbh
  is '测绘工程编号';
comment on column DCHY_XMGL_CLCG.chgcid
  is '测绘工程id';
comment on column DCHY_XMGL_CLCG.clcgmc
  is '测量成果名称';
comment on column DCHY_XMGL_CLCG.sjclid
  is '收件材料id';
comment on column DCHY_XMGL_CLCG.sjxxid
  is '收件信息id';
comment on column DCHY_XMGL_CLCG.wjzxid
  is '文件中心id';
comment on column DCHY_XMGL_CLCG.rksj
  is '入库时间';
comment on column DCHY_XMGL_CLCG.tjsj
  is '提交时间';
comment on column DCHY_XMGL_CLCG.tjr
  is '提交人id';
comment on column DCHY_XMGL_CLCG.tjrmc
  is '提交人名称';