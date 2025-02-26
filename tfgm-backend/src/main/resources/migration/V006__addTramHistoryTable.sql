CREATE TABLE tramhistory
(
  tramHistoryId uuid,
  tramId uuid,
  origin character varying(255),
  destination character varying(255),
  timeAtOrigin bigint,
  timeAtDestination bigint,
  status character varying(255),
  constraint tramhistory_pkey PRIMARY KEY (tramHistoryId),
  CONSTRAINT journey_tramuuid_fkey FOREIGN KEY (tramId)
    REFERENCES trams (uuid) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
)
