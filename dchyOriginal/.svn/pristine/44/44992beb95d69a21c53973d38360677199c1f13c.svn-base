insert into OM_USER (ID, ATTR, ENABLED, NAME, PASSWORD, VIEW_NAME) values ('0', '{}', 1, 'admin', '4aff8377ed06e9bcf3687b7724522a5c', 'admin');

alter table OM_ROLE add fixed NUMBER(1) default 1;
insert into OM_ROLE (ID, FIXED, ENABLED, NAME) values ('1', 1, 1, 'everyone');
insert into OM_ROLE (ID, FIXED, ENABLED, NAME) values ('2', 1, 1, 'guest');
insert into OM_ROLE (ID, FIXED, ENABLED, NAME) values ('3', 1, 1, 'user');
insert into OM_ROLE (ID, FIXED, ENABLED, NAME) values ('4', 1, 1, 'admin');

insert into M_HOST (ID, DESCRIPTION, ENABLED, NAME) values (1, '地图服务', 1, 'mapService');
insert into M_INTERFACE (ID, DESCRIPTION, ENABLED, NAME, ATTRS, HOST_ID) values (1, '地图服务', 1, 'mapService', null, 1);

create table M_HISTORY(
  ITEM_ID NUMBER(10) not null,
  CLOCK   NUMBER(12) not null,
  VALUE   FLOAT not null
);

create table M_HISTORY_INT(
  ITEM_ID NUMBER(10) not null,
  CLOCK   NUMBER(12) not null,
  VALUE   NUMBER(19) not null
);

create table M_TREND(
  ITEM_ID NUMBER(10) not null,
  CLOCK   NUMBER(12) not null,
  NUM     NUMBER(5) not null,
  V_MIN   FLOAT not null,
  V_MAX   FLOAT not null,
  V_AVG   FLOAT not null
);

create table M_TREND_INT(
  ITEM_ID NUMBER(10) not null,
  CLOCK   NUMBER(12) not null,
  NUM     NUMBER(5) not null,
  V_MIN   NUMBER(19) not null,
  V_MAX   NUMBER(19) not null,
  V_AVG   NUMBER(19) not null
);

create index M_HISTORY_IDX on M_HISTORY (ITEM_ID, CLOCK);
create index M_HISTORY_INT_IDX on M_HISTORY_INT (ITEM_ID, CLOCK);

create index M_TREND_IDX on M_TREND (ITEM_ID, CLOCK);
create index M_TREND_INT_IDX on M_TREND_INT (ITEM_ID, CLOCK);

create sequence HIBERNATE_SEQUENCE
minvalue 1
maxvalue 999999999999999999999999999
start with 1000
increment by 1
cache 20;