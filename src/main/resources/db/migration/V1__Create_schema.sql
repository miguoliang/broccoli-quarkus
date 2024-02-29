CREATE SEQUENCE edge_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE vertex_property_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE edge
(
    id            BIGINT       NOT NULL,
    in_vertex_id  VARCHAR(255) NOT NULL,
    out_vertex_id VARCHAR(255) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    scope         VARCHAR(255) NOT NULL,
    version       INT          NOT NULL,
    date_created  TIMESTAMP,
    date_updated  TIMESTAMP,
    CONSTRAINT pk_edge PRIMARY KEY (id)
);

CREATE TABLE vertex
(
    id           VARCHAR(255) NOT NULL,
    name         VARCHAR(255) NOT NULL,
    type         VARCHAR(255) NOT NULL,
    version      INT          NOT NULL,
    date_created TIMESTAMP,
    date_updated TIMESTAMP,
    CONSTRAINT pk_vertex PRIMARY KEY (id)
);

CREATE TABLE vertex_property
(
    id             BIGINT       NOT NULL,
    vertex_id      VARCHAR(255) NOT NULL,
    scope          VARCHAR(255) NOT NULL,
    property_key   VARCHAR(255) NOT NULL,
    property_value VARCHAR(255) NOT NULL,
    version        INT          NOT NULL,
    date_created   TIMESTAMP,
    date_updated   TIMESTAMP,
    CONSTRAINT pk_vertex_property PRIMARY KEY (id)
);

ALTER TABLE vertex_property
    ADD CONSTRAINT uc_007f8acf13bfbcddbece502e1 UNIQUE (vertex_id, scope, property_key);

ALTER TABLE edge
    ADD CONSTRAINT uc_083ea298d17ec030b58534aaa UNIQUE (in_vertex_id, out_vertex_id, name, scope);

ALTER TABLE edge
    ADD CONSTRAINT FK_EDGE_ON_IN_VERTEX FOREIGN KEY (in_vertex_id) REFERENCES vertex (id);

ALTER TABLE edge
    ADD CONSTRAINT FK_EDGE_ON_OUT_VERTEX FOREIGN KEY (out_vertex_id) REFERENCES vertex (id);

ALTER TABLE vertex_property
    ADD CONSTRAINT FK_VERTEX_PROPERTY_ON_VERTEX FOREIGN KEY (vertex_id) REFERENCES vertex (id);