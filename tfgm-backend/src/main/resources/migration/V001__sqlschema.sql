CREATE TABLE trams
(
  uuid        uuid NOT NULL,
  destination character varying(255) COLLATE pg_catalog."default",
  origin      character varying(255) COLLATE pg_catalog."default",
  endofline   character varying(255) COLLATE pg_catalog."default",
  tramhistory jsonb,
  lastupdated bigint,
  CONSTRAINT trams_pkey PRIMARY KEY (uuid)
);


CREATE TABLE tramnetwork
(
  uuid        uuid NOT NULL,
  timestamp bigint,
  tramjson    jsonb[],
  CONSTRAINT tramnetwork_pkey PRIMARY KEY (uuid)
);


CREATE TABLE people
(
  uuid       uuid NOT NULL,
  name       character varying(255) COLLATE pg_catalog."default",
  tapintime  bigint,
  tapinstop  character varying(255) COLLATE pg_catalog."default",
  tapouttime bigint,
  tapoutstop character varying(255) COLLATE pg_catalog."default",
  CONSTRAINT peopl_pkey PRIMARY KEY (uuid)
);

CREATE TABLE journeys
(
  uuid       uuid NOT NULL,
  getontime  bigint,
  getofftime bigint,
  tramuuid   uuid,
  getonstop  character varying(255) COLLATE pg_catalog."default",
  getoffstop character varying(255) COLLATE pg_catalog."default",
  personuuid uuid,
  CONSTRAINT journey_pkey PRIMARY KEY (uuid),
  CONSTRAINT journey_tramuuid_fkey FOREIGN KEY (tramuuid)
    REFERENCES trams (uuid) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
  CONSTRAINT journeys_personuuid_fkey FOREIGN KEY (personuuid)
    REFERENCES people (uuid) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);
