--create DATABASE IF NOT EXISTS "userDb" with OWNER = postgres ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C' TABLESPACE = pg_default CONNECTION LIMIT = -1;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--DROP TABLE users;
CREATE TABLE IF NOT EXISTS users (
    id uuid DEFAULT uuid_generate_v4 (),
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS messages (
  id                     VARCHAR(60)  DEFAULT uuid_generate_v4() PRIMARY KEY,
  content                VARCHAR      NOT NULL,
  content_type           VARCHAR(128) NOT NULL,
  sent                   TIMESTAMP    NOT NULL,
  username               VARCHAR(60)  NOT NULL,
  user_avatar_image_link VARCHAR(256) NOT NULL
);