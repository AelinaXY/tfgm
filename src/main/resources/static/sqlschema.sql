-- Table: public.trams

-- DROP TABLE IF EXISTS public.trams;

CREATE TABLE IF NOT EXISTS public.trams
(
  uuid        uuid NOT NULL,
  destination character varying(255) COLLATE pg_catalog."default",
  origin      character varying(255) COLLATE pg_catalog."default",
  endofline   character varying(255) COLLATE pg_catalog."default",
  tramhistory jsonb,
  lastupdated bigint,
  CONSTRAINT trams_pkey PRIMARY KEY (uuid)
)
  TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.trams
  OWNER to postgres;

-- Table: public.tramnetwork

-- DROP TABLE IF EXISTS public.tramnetwork;

CREATE TABLE IF NOT EXISTS public.tramnetwork
(
  uuid        uuid NOT NULL,
  "timestamp" bigint,
  tramjson    jsonb[],
  CONSTRAINT tramnetwork_pkey PRIMARY KEY (uuid)
)
  TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tramnetwork
  OWNER to postgres;

-- Table: public.people

-- DROP TABLE IF EXISTS public.people;

CREATE TABLE IF NOT EXISTS public.people
(
  uuid       uuid NOT NULL,
  name       character varying(255) COLLATE pg_catalog."default",
  tapintime  bigint,
  tapinstop  character varying(255) COLLATE pg_catalog."default",
  tapouttime bigint,
  tapoutstop character varying(255) COLLATE pg_catalog."default",
  CONSTRAINT peopl_pkey PRIMARY KEY (uuid)
)
  TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.people
  OWNER to postgres;

-- Table: public.journeys

-- DROP TABLE IF EXISTS public.journeys;

CREATE TABLE IF NOT EXISTS public.journeys
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
    REFERENCES public.trams (uuid) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
  CONSTRAINT journeys_personuuid_fkey FOREIGN KEY (personuuid)
    REFERENCES public.people (uuid) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID
)
  TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.journeys
  OWNER to postgres;
