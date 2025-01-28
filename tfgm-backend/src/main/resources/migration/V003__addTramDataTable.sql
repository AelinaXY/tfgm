CREATE TABLE IF NOT EXISTS tramdata
(
  timestamp bigint,
  response jsonb,
  constraint tramdata_pkey PRIMARY KEY (timestamp)
)
