CREATE TABLE journeytime
(
  uuid uuid,
  origin character varying(255),
  destination character varying(255),
  time bigint,
  averagecount bigint,
  constraint journeytime_pkey PRIMARY KEY (uuid)
)
