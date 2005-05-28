--
-- PostgreSQL database dump
--

SET client_encoding = 'UNICODE';
SET check_function_bodies = false;

SET SESSION AUTHORIZATION 'postgres';

--
-- TOC entry 4 (OID 2200)
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


SET SESSION AUTHORIZATION 'postgres';

SET search_path = public, pg_catalog;

--
-- TOC entry 9 (OID 17155)
-- Name: station; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE station (
    id serial NOT NULL,
    title character varying(50),
    "location" character varying(50),
    latitude real,
    longitude real,
    alias character varying(150)
) WITHOUT OIDS;


--
-- TOC entry 5 (OID 17159)
-- Name: temperature; Type: DOMAIN; Schema: public; Owner: postgres
--

CREATE DOMAIN temperature AS real DEFAULT 0
	CONSTRAINT in_range CHECK (((VALUE >= (-50)::double precision) AND (VALUE <= (60)::double precision)));


--
-- TOC entry 6 (OID 17161)
-- Name: humadity; Type: DOMAIN; Schema: public; Owner: postgres
--

CREATE DOMAIN humadity AS real DEFAULT 0
	CONSTRAINT "$1" CHECK (((VALUE >= (0)::double precision) AND (VALUE <= (100)::double precision)));


--
-- TOC entry 7 (OID 17163)
-- Name: pressureMB; Type: DOMAIN; Schema: public; Owner: postgres
--

CREATE DOMAIN "pressureMB" AS real
	CONSTRAINT "$2" CHECK ((((VALUE >= (800)::double precision) AND (VALUE <= (1100)::double precision)) OR (VALUE = (0)::double precision)));


--
-- TOC entry 10 (OID 17167)
-- Name: station_record; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE station_record (
    id serial NOT NULL,
    station integer,
    stamp timestamp without time zone,
    temperature temperature,
    humadity humadity,
    pressure "pressureMB",
    wind_speed real,
    wind_direction wind_direction
) WITHOUT OIDS;


--
-- TOC entry 8 (OID 17184)
-- Name: wind_direction; Type: DOMAIN; Schema: public; Owner: postgres
--

CREATE DOMAIN wind_direction AS real
	CONSTRAINT "$1" CHECK (((VALUE >= (0)::double precision) AND (VALUE <= (360)::double precision)));


--
-- TOC entry 11 (OID 17191)
-- Name: translation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE translation (
    id serial NOT NULL,
    lang character varying(5) NOT NULL,
    src_string character varying(255) NOT NULL,
    dst_string character varying(255)
) WITHOUT OIDS;


--
-- TOC entry 14 (OID 17170)
-- Name: k_station; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX k_station ON station_record USING btree (station);


--
-- TOC entry 13 (OID 17171)
-- Name: k_stamp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX k_stamp ON station_record USING btree (stamp);


--
-- TOC entry 17 (OID 17194)
-- Name: k_string; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX k_string ON translation USING btree (lang, src_string);


--
-- TOC entry 12 (OID 17172)
-- Name: station_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY station
    ADD CONSTRAINT station_pkey PRIMARY KEY (id);


--
-- TOC entry 15 (OID 17178)
-- Name: station_record_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY station_record
    ADD CONSTRAINT station_record_pkey PRIMARY KEY (id);


--
-- TOC entry 16 (OID 17195)
-- Name: k_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY translation
    ADD CONSTRAINT k_id PRIMARY KEY (id);


--
-- TOC entry 18 (OID 17174)
-- Name: $1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY station_record
    ADD CONSTRAINT "$1" FOREIGN KEY (station) REFERENCES station(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3 (OID 2200)
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'Standard public schema';


