create table if not exists trams
(
  uuid        uuid not null
    primary key,
  destination varchar(255),
  origin      varchar(255),
  endofline   varchar(255),
  tramhistory jsonb,
  lastupdated bigint,
  removed     boolean
);

alter table trams
  owner to postgres;

create table if not exists tramnetwork
(
  uuid      uuid not null
    primary key,
  timestamp bigint,
  tramjson  jsonb[]
);

alter table tramnetwork
  owner to postgres;

create table if not exists people
(
  uuid       uuid not null
    constraint peopl_pkey
      primary key,
  name       varchar(255),
  tapintime  bigint,
  tapinstop  varchar(255),
  tapouttime bigint,
  tapoutstop varchar(255)
);

alter table people
  owner to postgres;

create table if not exists journeys
(
  uuid       uuid not null
    constraint journey_pkey
      primary key,
  getontime  bigint,
  getofftime bigint,
  tramuuid   uuid
    constraint journey_tramuuid_fkey
      references trams,
  getonstop  varchar(255),
  getoffstop varchar(255),
  personuuid uuid
    references people
);

alter table journeys
  owner to postgres;

create table if not exists tramdata
(
  timestamp bigint not null
    primary key,
  response  jsonb
);

alter table tramdata
  owner to postgres;

create table if not exists journeytime
(
  origin       varchar(255),
  destination  varchar(255),
  time         bigint,
  averagecount bigint,
  uuid         uuid not null
    primary key
);

alter table journeytime
  owner to postgres;

create table if not exists tramdata_test
(
  timestamp bigint not null
    primary key,
  response  jsonb
);

alter table tramdata_test
  owner to postgres;
