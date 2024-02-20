CREATE TABLE edge
(
    in_vertex_id  VARCHAR(255) NOT NULL,
    out_vertex_id VARCHAR(255) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    scope         VARCHAR(255) NOT NULL,
    version       INT          NOT NULL,
    date_created  TIMESTAMP,
    date_updated  TIMESTAMP,
    CONSTRAINT pk_edge PRIMARY KEY (in_vertex_id, out_vertex_id, name, scope)
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
    vertex_id      VARCHAR(255) NOT NULL,
    scope          VARCHAR(255) NOT NULL,
    property_key   VARCHAR(255) NOT NULL,
    property_value VARCHAR(255) NOT NULL,
    version        INT          NOT NULL,
    date_created   TIMESTAMP,
    date_updated   TIMESTAMP,
    CONSTRAINT pk_vertex_property PRIMARY KEY (vertex_id, scope, property_key)
);

ALTER TABLE edge
    ADD CONSTRAINT FK_EDGE_ON_IN_VERTEX FOREIGN KEY (in_vertex_id) REFERENCES vertex (id);

ALTER TABLE edge
    ADD CONSTRAINT FK_EDGE_ON_OUT_VERTEX FOREIGN KEY (out_vertex_id) REFERENCES vertex (id);

ALTER TABLE vertex_property
    ADD CONSTRAINT FK_VERTEX_PROPERTY_ON_VERTEX FOREIGN KEY (vertex_id) REFERENCES vertex (id);