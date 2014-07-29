/* Database generated with pgModeler (PostgreSQL Database Modeler).
  Project Site: pgmodeler.com.br
  Model Author: --- */


/* Database creation must be done outside an multicommand file.
   These commands were put in this file only for convenience.

-- object: new_database | type: DATABASE -- 
CREATE DATABASE new_database
	ENCODING = 'UTF8'
;

*/

-- object: public.usuario | type: TABLE -- 
CREATE TABLE public.usuario(
	username varchar NOT NULL,
	nombre varchar NOT NULL,
	apellido varchar NOT NULL,
	email varchar NOT NULL,
	password varchar NOT NULL,
	CONSTRAINT pk_usuario PRIMARY KEY (username)
)
WITH (OIDS=FALSE);

-- object: public.usuario_rol | type: TABLE -- 
CREATE TABLE public.usuario_rol(
	username_usuario varchar,
	nombre_rol varchar,
	CONSTRAINT usuario_rol_pk PRIMARY KEY (username_usuario,nombre_rol)
)
WITH (OIDS=TRUE);

-- object: usuario_fk | type: CONSTRAINT -- 
ALTER TABLE public.usuario_rol ADD CONSTRAINT usuario_fk FOREIGN KEY (username_usuario)
REFERENCES public.usuario (username) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.rol | type: TABLE -- 
CREATE TABLE public.rol(
	nombre varchar,
	CONSTRAINT pk_rol PRIMARY KEY (nombre)
)
WITH (OIDS=FALSE);

-- object: rol_fk | type: CONSTRAINT -- 
ALTER TABLE public.usuario_rol ADD CONSTRAINT rol_fk FOREIGN KEY (nombre_rol)
REFERENCES public.rol (nombre) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.region | type: TABLE -- 
CREATE TABLE public.region(
	id serial,
	nombre varchar,
	CONSTRAINT pk_region PRIMARY KEY (id)
)
WITH (OIDS=FALSE);

-- object: public.servicio_salud | type: TABLE -- 
CREATE TABLE public.servicio_salud(
	id serial,
	nombre varchar,
	id_region integer,
	CONSTRAINT "Pk_servicio_salud" PRIMARY KEY (id)
)
WITH (OIDS=FALSE);

-- object: region_fk | type: CONSTRAINT -- 
ALTER TABLE public.servicio_salud ADD CONSTRAINT region_fk FOREIGN KEY (id_region)
REFERENCES public.region (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.establecimiento | type: TABLE -- 
CREATE TABLE public.establecimiento(
	id serial,
	nombre varchar,
	id_servicio_salud integer,
	id_comuna integer,
	CONSTRAINT pk_establecimiento PRIMARY KEY (id)
)
WITH (OIDS=FALSE);

-- object: servicio_salud_fk | type: CONSTRAINT -- 
ALTER TABLE public.establecimiento ADD CONSTRAINT servicio_salud_fk FOREIGN KEY (id_servicio_salud)
REFERENCES public.servicio_salud (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.comuna | type: TABLE -- 
CREATE TABLE public.comuna(
	id serial,
	nombre varchar,
	id_servicio_salud integer,
	CONSTRAINT pk_comuna PRIMARY KEY (id)
)
WITH (OIDS=FALSE);

-- object: servicio_salud_fk | type: CONSTRAINT -- 
ALTER TABLE public.comuna ADD CONSTRAINT servicio_salud_fk FOREIGN KEY (id_servicio_salud)
REFERENCES public.servicio_salud (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.tipo_programa | type: TABLE -- 
CREATE TABLE public.tipo_programa(
	id serial,
	nombre varchar,
	CONSTRAINT pk_tipo_programa PRIMARY KEY (id)
)
WITH (OIDS=FALSE);

-- object: public.programa | type: TABLE -- 
CREATE TABLE public.programa(
	id serial,
	nombre varchar,
	cantidad_cuotas smallint NOT NULL,
	id_tipo_programa integer,
	username_usuario varchar,
	CONSTRAINT pk_programa PRIMARY KEY (id)
)
WITH (OIDS=FALSE);

-- object: tipo_programa_fk | type: CONSTRAINT -- 
ALTER TABLE public.programa ADD CONSTRAINT tipo_programa_fk FOREIGN KEY (id_tipo_programa)
REFERENCES public.tipo_programa (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.componente | type: TABLE -- 
CREATE TABLE public.componente(
	id serial,
	nombre varchar,
	id_programa integer,
	CONSTRAINT pk_componente PRIMARY KEY (id)
)
WITH (OIDS=TRUE);

-- object: programa_fk | type: CONSTRAINT -- 
ALTER TABLE public.componente ADD CONSTRAINT programa_fk FOREIGN KEY (id_programa)
REFERENCES public.programa (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: comuna_fk | type: CONSTRAINT -- 
ALTER TABLE public.establecimiento ADD CONSTRAINT comuna_fk FOREIGN KEY (id_comuna)
REFERENCES public.comuna (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.programa_municipal_core | type: TABLE -- 
CREATE TABLE public.programa_municipal_core(
	columna1 varchar,
	columna2 varchar,
	columna3 varchar,
	columna4 varchar,
	columna5 varchar,
	columna6 varchar,
	columna7 varchar,
	columna8 varchar,
	columna9 varchar,
	columna10 varchar,
	id_programa integer,
	id_comuna integer,
	CONSTRAINT programa_municipal_core_pk PRIMARY KEY (id_programa,id_comuna)
)
WITH (OIDS=FALSE);

-- object: programa_fk | type: CONSTRAINT -- 
ALTER TABLE public.programa_municipal_core ADD CONSTRAINT programa_fk FOREIGN KEY (id_programa)
REFERENCES public.programa (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE NOT DEFERRABLE;

-- object: comuna_fk | type: CONSTRAINT -- 
ALTER TABLE public.programa_municipal_core ADD CONSTRAINT comuna_fk FOREIGN KEY (id_comuna)
REFERENCES public.comuna (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.metadata_core | type: TABLE -- 
CREATE TABLE public.metadata_core(
	id serial NOT NULL,
	indice_core smallint NOT NULL,
	descripcion varchar NOT NULL,
	id_programa integer,
	id_tipo_programa integer,
	CONSTRAINT pk_metadata_core PRIMARY KEY (id)
)
WITH (OIDS=FALSE);

-- object: programa_fk | type: CONSTRAINT -- 
ALTER TABLE public.metadata_core ADD CONSTRAINT programa_fk FOREIGN KEY (id_programa)
REFERENCES public.programa (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: tipo_programa_fk | type: CONSTRAINT -- 
ALTER TABLE public.metadata_core ADD CONSTRAINT tipo_programa_fk FOREIGN KEY (id_tipo_programa)
REFERENCES public.tipo_programa (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.programa_servicio_core | type: TABLE -- 
CREATE TABLE public.programa_servicio_core(
	columna1 varchar,
	columna2 varchar,
	columna3 varchar,
	columna4 varchar,
	columna5 varchar,
	columna6 varchar,
	columna7 varchar,
	columna8 varchar,
	columna9 varchar,
	columna10 varchar,
	id_programa_servicio serial,
	id_programa integer,
	id_servicio_salud integer,
	CONSTRAINT pk_programa_servicio PRIMARY KEY (id_programa_servicio)
)
WITH (OIDS=FALSE);

-- object: programa_fk | type: CONSTRAINT -- 
ALTER TABLE public.programa_servicio_core ADD CONSTRAINT programa_fk FOREIGN KEY (id_programa)
REFERENCES public.programa (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: servicio_salud_fk | type: CONSTRAINT -- 
ALTER TABLE public.programa_servicio_core ADD CONSTRAINT servicio_salud_fk FOREIGN KEY (id_servicio_salud)
REFERENCES public.servicio_salud (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: usuario_fk | type: CONSTRAINT -- 
ALTER TABLE public.programa ADD CONSTRAINT usuario_fk FOREIGN KEY (username_usuario)
REFERENCES public.usuario (username) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.ano_en_curso | type: TABLE -- 
CREATE TABLE public.ano_en_curso(
	ano smallint NOT NULL,
	monto_percapital_basal smallint NOT NULL,
	CONSTRAINT pk_ano_en_curso PRIMARY KEY (ano)
)
WITH (OIDS=FALSE);

-- object: public.antecendentes_comuna | type: TABLE -- 
CREATE TABLE public.antecendentes_comuna(
	clasificacion varchar NOT NULL,
	asignacion_zona smallint NOT NULL,
	poblacion smallint NOT NULL,
	poblacion_mayor smallint NOT NULL,
	desempeno_dificil smallint NOT NULL,
	tramo_pobreza smallint NOT NULL,
	pobreza smallint NOT NULL,
	ruralidad smallint NOT NULL,
	valor_referencial_zona smallint NOT NULL,
	ano_ano_en_curso smallint,
	id_comuna integer,
	CONSTRAINT antecendentes_comuna_pk PRIMARY KEY (ano_ano_en_curso,id_comuna)
)
WITH (OIDS=FALSE);

-- object: ano_en_curso_fk | type: CONSTRAINT -- 
ALTER TABLE public.antecendentes_comuna ADD CONSTRAINT ano_en_curso_fk FOREIGN KEY (ano_ano_en_curso)
REFERENCES public.ano_en_curso (ano) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE NOT DEFERRABLE;

-- object: comuna_fk | type: CONSTRAINT -- 
ALTER TABLE public.antecendentes_comuna ADD CONSTRAINT comuna_fk FOREIGN KEY (id_comuna)
REFERENCES public.comuna (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.cuota | type: TABLE -- 
CREATE TABLE public.cuota(
	id serial NOT NULL,
	numero_cuota smallint NOT NULL,
	monto smallint NOT NULL,
	fecha_pago1 smallint NOT NULL,
	fecha_pago2 smallint NOT NULL,
	fecha_pago3 smallint NOT NULL,
	id_programa integer,
	CONSTRAINT pk_cuota PRIMARY KEY (id)
)
WITH (OIDS=FALSE);

-- object: programa_fk | type: CONSTRAINT -- 
ALTER TABLE public.cuota ADD CONSTRAINT programa_fk FOREIGN KEY (id_programa)
REFERENCES public.programa (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.marco_presupuestario | type: TABLE -- 
CREATE TABLE public.marco_presupuestario(
	marco_inicial smallint NOT NULL,
	marco_modificado smallint NOT NULL,
	id_marco_presupuestario serial,
	ano_ano_en_curso smallint,
	id_programa integer,
	id_servicio_salud integer,
	CONSTRAINT pk_marco_presupuestario PRIMARY KEY (id_marco_presupuestario)
)
WITH (OIDS=FALSE);

-- object: ano_en_curso_fk | type: CONSTRAINT -- 
ALTER TABLE public.marco_presupuestario ADD CONSTRAINT ano_en_curso_fk FOREIGN KEY (ano_ano_en_curso)
REFERENCES public.ano_en_curso (ano) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: programa_fk | type: CONSTRAINT -- 
ALTER TABLE public.marco_presupuestario ADD CONSTRAINT programa_fk FOREIGN KEY (id_programa)
REFERENCES public.programa (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: servicio_salud_fk | type: CONSTRAINT -- 
ALTER TABLE public.marco_presupuestario ADD CONSTRAINT servicio_salud_fk FOREIGN KEY (id_servicio_salud)
REFERENCES public.servicio_salud (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.seguimiento | type: TABLE -- 
CREATE TABLE public.seguimiento(
	id serial NOT NULL,
	id_instancia smallint NOT NULL,
	mail_from varchar NOT NULL,
	mail_to varchar NOT NULL,
	subject varchar NOT NULL,
	cc varchar,
	cco varchar,
	body varchar(4),
	fecha_envio timestamp NOT NULL,
	id_programa integer,
	CONSTRAINT pk_seguimiento PRIMARY KEY (id)
)
WITH (OIDS=TRUE);

-- object: programa_fk | type: CONSTRAINT -- 
ALTER TABLE public.seguimiento ADD CONSTRAINT programa_fk FOREIGN KEY (id_programa)
REFERENCES public.programa (id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.seguimiento_referencia_documento | type: TABLE -- 
CREATE TABLE public.seguimiento_referencia_documento(
	id_seguimiento integer,
	id_referencia_documento integer,
	CONSTRAINT seguimiento_referencia_documento_pk PRIMARY KEY (id_seguimiento,id_referencia_documento)
)
WITH (OIDS=TRUE);

-- object: seguimiento_fk | type: CONSTRAINT -- 
ALTER TABLE public.seguimiento_referencia_documento ADD CONSTRAINT seguimiento_fk FOREIGN KEY (id_seguimiento)
REFERENCES public.seguimiento (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE NOT DEFERRABLE;

-- object: public.referencia_documento | type: TABLE -- 
CREATE TABLE public.referencia_documento(
	id serial,
	content_type text,
  	path text,
        documento_final boolean,
	CONSTRAINT pk_referencia_documento PRIMARY KEY (id)
)
WITH (OIDS=TRUE);

-- object: referencia_documento_fk | type: CONSTRAINT -- 
ALTER TABLE public.seguimiento_referencia_documento ADD CONSTRAINT referencia_documento_fk FOREIGN KEY (id_referencia_documento)
REFERENCES public.referencia_documento (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE NOT DEFERRABLE;



CREATE SEQUENCE distribucion_inicial_percapit_id_distribucion_inicial_perca_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;



CREATE TABLE distribucion_inicial_percapita
(
  id_distribucion_inicial_percapita integer NOT NULL DEFAULT nextval('distribucion_inicial_percapit_id_distribucion_inicial_perca_seq'::regclass),
  usuario text,
  fecha_creacion timestamp with time zone,
  CONSTRAINT id_distribucion_pk PRIMARY KEY (id_distribucion_inicial_percapita),
  CONSTRAINT usuario_fk FOREIGN KEY (usuario)
      REFERENCES usuario (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE documento_distribucion_inicial_percapita
(
  id_distribucion_inicial_percapita integer NOT NULL,
  id_documento integer NOT NULL,
  CONSTRAINT documento_distribucion_inicial_percapita_pk PRIMARY KEY (id_distribucion_inicial_percapita, id_documento),
  CONSTRAINT distribucion_inicial_percapita_fk FOREIGN KEY (id_distribucion_inicial_percapita)
      REFERENCES distribucion_inicial_percapita (id_distribucion_inicial_percapita) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT documento_fk FOREIGN KEY (id_documento)
      REFERENCES referencia_documento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


CREATE SEQUENCE hibernate_sequence
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1; 

CREATE TABLE tipo_plantilla
(
  id_tipo_plantilla serial NOT NULL,
  descripcion text NOT NULL,
  CONSTRAINT plantilla_pk PRIMARY KEY (id_tipo_plantilla)
)
WITH (
  OIDS=FALSE
);


CREATE TABLE plantilla
(
  id_plantilla serial NOT NULL,
  fecha_creacion timestamp without time zone NOT NULL,
  tipo_plantilla integer NOT NULL,
  fecha_vigencia timestamp without time zone,
  documento integer,
  CONSTRAINT id_plantilla_pk PRIMARY KEY (id_plantilla),
  CONSTRAINT documento_fk FOREIGN KEY (documento)
      REFERENCES referencia_documento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT tipo_plantilla_fk FOREIGN KEY (tipo_plantilla)
      REFERENCES tipo_plantilla (id_tipo_plantilla) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


INSERT INTO tipo_plantilla(id_tipo_plantilla, descripcion) VALUES (1, 'Asignación de Desempeño Difícil');
INSERT INTO tipo_plantilla(id_tipo_plantilla, descripcion) VALUES (2, 'Población Inscrita Validada');

ALTER TABLE referencia_documento
   ADD COLUMN node_ref text;

-- Modificaciones 24/07/2014

CREATE TABLE tramo
(
  id_tramo serial NOT NULL,
  tramo text NOT NULL,
  CONSTRAINT id_tramo_pk PRIMARY KEY (id_tramo)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE tipo_cumplimiento
(
  id_tipo_cumplimiento serial NOT NULL,
  descripcion text NOT NULL,
  CONSTRAINT tipo_cumplimiento_pk PRIMARY KEY (id_tipo_cumplimiento)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE cumplimiento
(
  id_cumplimiento serial NOT NULL,
  tramo integer NOT NULL,
  tipo_cumplimiento integer NOT NULL,
  rebaja numeric NOT NULL,
  porcentaje_desde numeric NOT NULL,
  porcentaje_hasta numeric,
  CONSTRAINT id_cumplimiento_pk PRIMARY KEY (id_cumplimiento),
  CONSTRAINT tipo_cumplimiento_fk FOREIGN KEY (tipo_cumplimiento)
      REFERENCES tipo_cumplimiento (id_tipo_cumplimiento) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT tramo_fk FOREIGN KEY (tramo)
      REFERENCES tramo (id_tramo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Modificacion tabla antecendentes_comuna

ALTER TABLE antecendentes_comuna DROP COLUMN poblacion;
ALTER TABLE antecendentes_comuna DROP COLUMN poblacion_mayor;
ALTER TABLE antecendentes_comuna DROP COLUMN desempeno_dificil;
ALTER TABLE antecendentes_comuna DROP COLUMN pobreza;
ALTER TABLE antecendentes_comuna DROP COLUMN ruralidad;
ALTER TABLE antecendentes_comuna DROP COLUMN valor_referencial_zona;
ALTER TABLE antecendentes_comuna DROP CONSTRAINT antecendentes_comuna_pk;
ALTER TABLE antecendentes_comuna DROP CONSTRAINT ano_en_curso_fk;
ALTER TABLE antecendentes_comuna DROP CONSTRAINT comuna_fk;
ALTER TABLE antecendentes_comuna
ADD COLUMN id_antecedentes_comuna serial NOT NULL;
ALTER TABLE antecendentes_comuna
  ADD CONSTRAINT antecendentes_comuna_pk PRIMARY KEY(id_antecedentes_comuna);
ALTER TABLE antecendentes_comuna
  ADD CONSTRAINT comuna_fk FOREIGN KEY (id_comuna) REFERENCES comuna (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_comuna_fk
  ON antecendentes_comuna(id_comuna);
ALTER TABLE antecendentes_comuna
  ADD CONSTRAINT ano_en_curso_fk FOREIGN KEY (ano_ano_en_curso) REFERENCES ano_en_curso (ano)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_ano_en_curso_fk
  ON antecendentes_comuna(ano_ano_en_curso);

CREATE TABLE antecendentes_comuna_calculado
(
  id_antecendentes_comuna_calculado serial NOT NULL,
  antecedentes_comuna integer NOT NULL,
  poblacion smallint,
  poblacion_mayor smallint,
  desempeno_dificil smallint,
  pobreza smallint,
  ruralidad smallint,
  valor_referencial_zona smallint,
  CONSTRAINT antecendentes_comuna_calculado_pk PRIMARY KEY (id_antecendentes_comuna_calculado),
  CONSTRAINT antecendentes_comuna_fk FOREIGN KEY (antecedentes_comuna)
      REFERENCES antecendentes_comuna (id_antecedentes_comuna) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);


--Modificaciones 25/07/2014
CREATE TABLE mes
(
 id_mes serial NOT NULL,
 nombre text,
 CONSTRAINT mes_pkey PRIMARY KEY (id_mes)
)
WITH (
 OIDS=FALSE
);

CREATE TABLE comuna_cumplimiento
(
 id_comuna_cumplimiento serial NOT NULL,
 id_comuna integer,
 id_tipo_cumplimiento integer,
 valor integer,
 id_mes integer,
 CONSTRAINT comuna_cumplimiento_pkey PRIMARY KEY (id_comuna_cumplimiento),
 CONSTRAINT comuna_cumplimiento_id_comuna_fkey FOREIGN KEY (id_comuna)
     REFERENCES comuna (id) MATCH SIMPLE
     ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT comuna_cumplimiento_id_mes_fkey FOREIGN KEY (id_mes)
     REFERENCES mes (id_mes) MATCH SIMPLE
     ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT comuna_cumplimiento_id_tipo_cumplimiento_fkey FOREIGN KEY (id_tipo_cumplimiento)
     REFERENCES tipo_cumplimiento (id_tipo_cumplimiento) MATCH SIMPLE
     ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
 OIDS=FALSE
);


CREATE TABLE comuna_rebaja
(
 id_comuna_rebaja serial NOT NULL,
 comuna_cumplimiento integer,
 rebaja_calculada double precision,
 rebaja_final double precision,
 CONSTRAINT comuna_rebaja_pkey PRIMARY KEY (id_comuna_rebaja),
 CONSTRAINT comuna_rebaja_comuna_cumplimiento_fkey FOREIGN KEY (comuna_cumplimiento)
     REFERENCES comuna_cumplimiento (id_comuna_cumplimiento) MATCH SIMPLE
     ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
 OIDS=FALSE
);

--cambios sabado 26/07/2014

ALTER TABLE antecendentes_comuna_calculado
   ALTER COLUMN poblacion TYPE integer;

ALTER TABLE antecendentes_comuna_calculado
   ALTER COLUMN poblacion_mayor TYPE integer;

ALTER TABLE antecendentes_comuna_calculado
   ALTER COLUMN desempeno_dificil TYPE integer;

ALTER TABLE antecendentes_comuna_calculado ADD COLUMN distribucion_inicial_percapita integer;

ALTER TABLE antecendentes_comuna_calculado
  ADD CONSTRAINT distribucion_inicial_percapita_fk FOREIGN KEY (distribucion_inicial_percapita)
      REFERENCES distribucion_inicial_percapita (id_distribucion_inicial_percapita) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE antecendentes_comuna_calculado ALTER COLUMN distribucion_inicial_percapita SET NOT NULL;

CREATE TABLE factor_tramo_pobreza
(
  id_factor_tramo_pobreza serial NOT NULL,
  valor numeric NOT NULL,
  CONSTRAINT factor_tramo_pobreza_pk PRIMARY KEY (id_factor_tramo_pobreza)
)
WITH (
  OIDS=FALSE
);

INSERT INTO factor_tramo_pobreza(id_factor_tramo_pobreza, valor) VALUES (1, 0.18);
INSERT INTO factor_tramo_pobreza(id_factor_tramo_pobreza, valor) VALUES (2, 0.12);
INSERT INTO factor_tramo_pobreza(id_factor_tramo_pobreza, valor) VALUES (3, 0.06);
INSERT INTO factor_tramo_pobreza(id_factor_tramo_pobreza, valor) VALUES (4, 0.0);


UPDATE antecendentes_comuna
   SET tramo_pobreza=NULL
 WHERE tramo_pobreza = 0;

ALTER TABLE antecendentes_comuna
  ADD CONSTRAINT tramo_pobreza_fk FOREIGN KEY (tramo_pobreza)
      REFERENCES factor_tramo_pobreza (id_factor_tramo_pobreza) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;



ALTER TABLE antecendentes_comuna_calculado
   ALTER COLUMN pobreza TYPE numeric;

ALTER TABLE antecendentes_comuna_calculado
   ALTER COLUMN ruralidad TYPE numeric;

ALTER TABLE antecendentes_comuna_calculado
   ALTER COLUMN valor_referencial_zona TYPE numeric;

CREATE TABLE factor_ref_asig_zona
(
  id_factor_ref_asig_zona serial NOT NULL,
  zona smallint NOT NULL,
  valor numeric NOT NULL,
  CONSTRAINT factor_ref_asig_zona_pk PRIMARY KEY (id_factor_ref_asig_zona)
)
WITH (
  OIDS=FALSE
);

INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (1, 105, 0.24);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (2, 95, 0.24);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (3, 90, 0.24);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (4, 85, 0.24);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (5, 80, 0.24);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (6, 70, 0.24);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (7, 55, 0.19);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (8, 50, 0.14);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (9, 40, 0.14);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (10, 35, 0.12);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (11, 30, 0.1);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (12, 25, 0.09);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (13, 20, 0.07);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (14, 15, 0.05);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (15, 10, 0.04);
INSERT INTO factor_ref_asig_zona(id_factor_ref_asig_zona, zona, valor) VALUES (16, 0, 0.0);

UPDATE antecendentes_comuna SET asignacion_zona=9 WHERE asignacion_zona = 40;
UPDATE antecendentes_comuna SET asignacion_zona=7 WHERE asignacion_zona = 55;
UPDATE antecendentes_comuna SET asignacion_zona=5 WHERE asignacion_zona = 80;
UPDATE antecendentes_comuna SET asignacion_zona=11 WHERE asignacion_zona = 30;
UPDATE antecendentes_comuna SET asignacion_zona=10 WHERE asignacion_zona = 35;
UPDATE antecendentes_comuna SET asignacion_zona=8 WHERE asignacion_zona = 50;
UPDATE antecendentes_comuna SET asignacion_zona=12 WHERE asignacion_zona = 25;
UPDATE antecendentes_comuna SET asignacion_zona=15 WHERE asignacion_zona = 10;
UPDATE antecendentes_comuna SET asignacion_zona=14 WHERE asignacion_zona = 15;
UPDATE antecendentes_comuna SET asignacion_zona=16 WHERE asignacion_zona = 0;
UPDATE antecendentes_comuna SET asignacion_zona=13 WHERE asignacion_zona = 20;
UPDATE antecendentes_comuna SET asignacion_zona=3 WHERE asignacion_zona = 90;
UPDATE antecendentes_comuna SET asignacion_zona=6 WHERE asignacion_zona = 70;
UPDATE antecendentes_comuna SET asignacion_zona=1 WHERE asignacion_zona = 105;
UPDATE antecendentes_comuna SET asignacion_zona=2 WHERE asignacion_zona = 95;
UPDATE antecendentes_comuna SET asignacion_zona=4 WHERE asignacion_zona = 85;

ALTER TABLE antecendentes_comuna
  ADD CONSTRAINT asignacion_zona_fk FOREIGN KEY (asignacion_zona)
      REFERENCES factor_ref_asig_zona (id_factor_ref_asig_zona) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE antecendentes_comuna_calculado
   ALTER COLUMN distribucion_inicial_percapita DROP NOT NULL;

INSERT INTO ano_en_curso(ano, monto_percapital_basal) VALUES (2013, 3794);

INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	2102	,	400	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	2301	,	401	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	4103	,	402	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	 NULL	,	 NULL	,	2013	,	5502	,	403	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	5505	,	404	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	5703	,	405	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	6103	,	406	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	6106	,	407	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	 NULL	,	 NULL	,	2013	,	8420	,	408	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	10403	,	409	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	11401	,	410	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	11301	,	411	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	11101	,	412	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	11203	,	413	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	11102	,	414	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	11302	,	415	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	11201	,	416	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	11202	,	417	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	11402	,	418	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	11303	,	419	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	12202	,	420	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	12301	,	421	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	12302	,	422	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	5201	,	423	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	13102	,	424	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	13106	,	425	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	 NULL	,	 NULL	,	2013	,	13119	,	426	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	 NULL	,	 NULL	,	2013	,	13999	,	427	);
INSERT INTO antecendentes_comuna VALUES	(''	,	 NULL	,	 NULL	,	2013	,	13503	,	428	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	 NULL	,	 NULL	,	2013	,	13131	,	429	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	9	,	4	,	2013	,	15101	,	430	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	9	,	4	,	2013	,	1107	,	431	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	9	,	4	,	2013	,	1101	,	432	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	7	,	4	,	2013	,	1401	,	433	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	11	,	4	,	2013	,	2101	,	434	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	12	,	3	,	2013	,	3102	,	435	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	12	,	4	,	2013	,	3201	,	436	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	12	,	4	,	2013	,	3101	,	437	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	1	,	2013	,	3303	,	438	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	4	,	2013	,	3304	,	439	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	12	,	4	,	2013	,	3301	,	440	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	9	,	1	,	2013	,	10202	,	441	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	9	,	4	,	2013	,	10201	,	442	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	9	,	3	,	2013	,	10203	,	443	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	9	,	1	,	2013	,	10204	,	444	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	9	,	3	,	2013	,	10205	,	445	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	7	,	2	,	2013	,	9205	,	446	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	4	,	2013	,	8202	,	447	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	3	,	2013	,	8203	,	448	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	12	,	1	,	2013	,	8204	,	449	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	12	,	4	,	2013	,	8205	,	450	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	12	,	4	,	2013	,	8201	,	451	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	2	,	2013	,	8206	,	452	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	2	,	2013	,	8207	,	453	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	4	,	2013	,	8309	,	454	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	2	,	2013	,	8312	,	455	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	1	,	2013	,	8403	,	456	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	9	,	2	,	2013	,	10208	,	457	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	9	,	1	,	2013	,	10209	,	458	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	9	,	 NULL	,	2013	,	2103	,	459	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	9	,	 NULL	,	2013	,	2104	,	460	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	9	,	 NULL	,	2013	,	10206	,	461	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	9	,	 NULL	,	2013	,	10207	,	462	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	7	,	 NULL	,	2013	,	15102	,	463	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	7	,	 NULL	,	2013	,	15202	,	464	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	7	,	 NULL	,	2013	,	1404	,	465	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	7	,	 NULL	,	2013	,	1405	,	466	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	5	,	 NULL	,	2013	,	15201	,	467	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	5	,	 NULL	,	2013	,	1402	,	468	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	5	,	 NULL	,	2013	,	1403	,	469	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	5	,	 NULL	,	2013	,	2202	,	470	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	11	,	4	,	2013	,	10304	,	471	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	8	,	 NULL	,	2013	,	2203	,	472	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	1	,	2013	,	8407	,	473	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	1	,	2013	,	8408	,	474	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	1	,	2013	,	8409	,	475	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	1	,	2013	,	8410	,	476	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	1	,	2013	,	8412	,	477	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	4	,	2013	,	8416	,	478	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	12	,	1	,	2013	,	8417	,	479	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	3	,	2013	,	8419	,	480	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	4	,	2013	,	8421	,	481	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	12	,	1	,	2013	,	10306	,	482	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	12	,	 NULL	,	2013	,	8414	,	483	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	4302	,	484	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	2	,	2013	,	4201	,	485	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	3	,	2013	,	4203	,	486	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	4301	,	487	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	4204	,	488	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	4202	,	489	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	14	,	4	,	2013	,	4102	,	490	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	14	,	4	,	2013	,	4101	,	491	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	3	,	2013	,	4303	,	492	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	2	,	2013	,	4304	,	493	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	2	,	2013	,	4305	,	494	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	2	,	2013	,	4106	,	495	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	8304	,	496	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	8301	,	497	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	8305	,	498	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	8306	,	499	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	8307	,	500	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	8311	,	501	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	8402	,	502	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	14	,	4	,	2013	,	8401	,	503	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	14	,	4	,	2013	,	8406	,	504	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	8405	,	505	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	8411	,	506	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	8413	,	507	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	8418	,	508	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	14	,	2	,	2013	,	9201	,	509	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	9202	,	510	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9204	,	511	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	3	,	2013	,	9206	,	512	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9207	,	513	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9209	,	514	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	3	,	2013	,	9210	,	515	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	9211	,	516	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9102	,	517	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9121	,	518	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9103	,	519	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9104	,	520	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	3	,	2013	,	9105	,	521	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9106	,	522	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9107	,	523	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	3	,	2013	,	9108	,	524	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9110	,	525	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9111	,	526	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	2	,	2013	,	9112	,	527	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9113	,	528	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	3	,	2013	,	9114	,	529	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	14	,	4	,	2013	,	9101	,	530	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9117	,	531	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	9119	,	532	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	14	,	4	,	2013	,	10101	,	533	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	14	,	4	,	2013	,	10301	,	534	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	10302	,	535	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	10303	,	536	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	10305	,	537	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	10307	,	538	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	2	,	2013	,	14202	,	539	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	14201	,	540	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	14203	,	541	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	14103	,	542	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	14104	,	543	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	2	,	2013	,	14105	,	544	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	14106	,	545	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	14107	,	546	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	14108	,	547	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	14204	,	548	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	14	,	4	,	2013	,	14101	,	549	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	14	,	 NULL	,	2013	,	8302	,	550	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	14	,	 NULL	,	2013	,	8308	,	551	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	14	,	 NULL	,	2013	,	14102	,	552	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	14	,	4	,	2013	,	2201	,	553	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	2	,	2013	,	3302	,	554	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	14	,	1	,	2013	,	3202	,	555	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	4	,	2013	,	3103	,	556	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	2	,	2013	,	7201	,	557	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	7203	,	558	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	14	,	1	,	2013	,	8314	,	559	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	14	,	 NULL	,	2013	,	4104	,	560	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	14	,	 NULL	,	2013	,	4105	,	561	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	14	,	 NULL	,	2013	,	7202	,	562	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	14	,	 NULL	,	2013	,	2302	,	563	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	14	,	 NULL	,	2013	,	9203	,	564	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	14	,	 NULL	,	2013	,	10103	,	565	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5302	,	566	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	2	,	2013	,	5702	,	567	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	5301	,	568	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5704	,	569	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	3	,	2013	,	5705	,	570	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5303	,	571	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5304	,	572	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5701	,	573	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	3	,	2013	,	5706	,	574	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5602	,	575	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	5603	,	576	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	5604	,	577	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5605	,	578	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	5601	,	579	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5606	,	580	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	5101	,	581	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5402	,	582	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	5103	,	583	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	2	,	2013	,	5503	,	584	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	3	,	2013	,	5504	,	585	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5401	,	586	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5506	,	587	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5507	,	588	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	1	,	2013	,	5403	,	589	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	1	,	2013	,	5404	,	590	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5105	,	591	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	5501	,	592	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	5106	,	593	);
INSERT INTO antecendentes_comuna VALUES	(''	,	16	,	4	,	2013	,	5107	,	594	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	5108	,	595	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	5109	,	596	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	5405	,	597	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6302	,	598	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6303	,	599	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6102	,	600	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	3	,	2013	,	6104	,	601	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	1	,	2013	,	6105	,	602	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6107	,	603	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	1	,	2013	,	6304	,	604	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	6108	,	605	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6109	,	606	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6110	,	607	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6305	,	608	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	1	,	2013	,	6205	,	609	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6111	,	610	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6306	,	611	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	1	,	2013	,	6206	,	612	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6307	,	613	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	4	,	2013	,	6112	,	614	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6113	,	615	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6201	,	616	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	2	,	2013	,	6308	,	617	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	3	,	2013	,	6114	,	618	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	6101	,	619	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6115	,	620	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6116	,	621	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	6301	,	622	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6117	,	623	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	6310	,	624	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	1	,	2013	,	7402	,	625	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	7102	,	626	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	7301	,	627	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	7401	,	628	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	1	,	2013	,	7403	,	629	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	2	,	2013	,	7105	,	630	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	7304	,	631	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	7404	,	632	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	7106	,	633	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	1	,	2013	,	7107	,	634	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	2	,	2013	,	7305	,	635	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	2	,	2013	,	7405	,	636	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	7108	,	637	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	7306	,	638	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	7307	,	639	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	2	,	2013	,	7109	,	640	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	7406	,	641	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	7110	,	642	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	7101	,	643	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	7308	,	644	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	7309	,	645	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	1	,	2013	,	7407	,	646	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	1	,	2013	,	7408	,	647	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13301	,	648	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13104	,	649	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13107	,	650	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13108	,	651	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	13302	,	652	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13125	,	653	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13127	,	654	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	13303	,	655	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	1	,	2013	,	13502	,	656	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	2	,	2013	,	13103	,	657	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	13602	,	658	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	2	,	2013	,	13603	,	659	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	1	,	2013	,	13117	,	660	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	13504	,	661	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	13501	,	662	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	13604	,	663	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	13605	,	664	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13124	,	665	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13126	,	666	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13128	,	667	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	1	,	2013	,	13505	,	668	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	13601	,	669	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13101	,	670	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13113	,	671	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13114	,	672	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13115	,	673	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13118	,	674	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13120	,	675	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13122	,	676	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13123	,	677	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13132	,	678	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13402	,	679	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	13403	,	680	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	1	,	2013	,	13105	,	681	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13109	,	682	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	2	,	2013	,	13998	,	683	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	2	,	2013	,	13116	,	684	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	13404	,	685	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	3	,	2013	,	13121	,	686	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13401	,	687	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13129	,	688	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	4	,	2013	,	13130	,	689	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	3	,	2013	,	13110	,	690	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	2	,	2013	,	13111	,	691	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	1	,	2013	,	13112	,	692	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	16	,	4	,	2013	,	13202	,	693	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	16	,	3	,	2013	,	13201	,	694	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	 NULL	,	2013	,	5102	,	695	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	 NULL	,	2013	,	5104	,	696	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	 NULL	,	2013	,	6202	,	697	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	 NULL	,	2013	,	6203	,	698	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	 NULL	,	2013	,	6204	,	699	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	 NULL	,	2013	,	6309	,	700	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	 NULL	,	2013	,	7103	,	701	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	 NULL	,	2013	,	7104	,	702	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	 NULL	,	2013	,	7302	,	703	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	 NULL	,	2013	,	7303	,	704	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	16	,	 NULL	,	2013	,	13203	,	705	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	4	,	2013	,	8303	,	706	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	13	,	1	,	2013	,	8310	,	707	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	1	,	2013	,	8313	,	708	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	13	,	4	,	2013	,	8103	,	709	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	13	,	4	,	2013	,	8101	,	710	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	13	,	4	,	2013	,	8102	,	711	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	1	,	2013	,	8104	,	712	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	2	,	2013	,	8105	,	713	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	13	,	4	,	2013	,	8106	,	714	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	13	,	4	,	2013	,	8108	,	715	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	2	,	2013	,	8109	,	716	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	1	,	2013	,	8415	,	717	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	13	,	3	,	2013	,	8112	,	718	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	13	,	4	,	2013	,	8107	,	719	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	13	,	4	,	2013	,	8110	,	720	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	4	,	2013	,	8111	,	721	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	2	,	2013	,	9109	,	722	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	4	,	2013	,	9115	,	723	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	1	,	2013	,	9116	,	724	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	1	,	2013	,	9118	,	725	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	4	,	2013	,	9120	,	726	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	3	,	2013	,	10102	,	727	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	4	,	2013	,	10104	,	728	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	4	,	2013	,	10105	,	729	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	13	,	4	,	2013	,	10107	,	730	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	3	,	2013	,	10106	,	731	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	13	,	2	,	2013	,	10108	,	732	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	13	,	4	,	2013	,	10109	,	733	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	13	,	 NULL	,	2013	,	8404	,	734	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	13	,	 NULL	,	2013	,	9208	,	735	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	3	,	 NULL	,	2013	,	10401	,	736	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	3	,	 NULL	,	2013	,	10402	,	737	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	3	,	 NULL	,	2013	,	10404	,	738	);
INSERT INTO antecendentes_comuna VALUES	('RURAL'	,	6	,	1	,	2013	,	10210	,	739	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	6	,	4	,	2013	,	12101	,	740	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	6	,	 NULL	,	2013	,	12103	,	741	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	6	,	 NULL	,	2013	,	12104	,	742	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	6	,	 NULL	,	2013	,	12402	,	743	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	1	,	 NULL	,	2013	,	12201	,	744	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	2	,	 NULL	,	2013	,	12102	,	745	);
INSERT INTO antecendentes_comuna VALUES	('URBANA'	,	4	,	 NULL	,	2013	,	12401	,	746	);
INSERT INTO antecendentes_comuna VALUES	('COSTO FIJO'	,	4	,	 NULL	,	2013	,	12303	,	747	);


INSERT INTO antecendentes_comuna_calculado VALUES (	1000	,	400	,	30014	,	50014	,	55014	,	NULL	,	NULL	,	NULL	,	NULL	,	13000	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1001	,	401	,	30019	,	50019	,	55019	,	NULL	,	NULL	,	NULL	,	NULL	,	13001	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1002	,	402	,	30029	,	50029	,	55029	,	NULL	,	NULL	,	NULL	,	NULL	,	13002	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1003	,	403	,	30054	,	50054	,	55054	,	NULL	,	NULL	,	NULL	,	NULL	,	13003	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1004	,	404	,	30059	,	50059	,	55059	,	NULL	,	NULL	,	NULL	,	NULL	,	13004	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1005	,	405	,	30073	,	50073	,	55073	,	NULL	,	NULL	,	NULL	,	NULL	,	13005	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1006	,	406	,	30084	,	50084	,	55084	,	NULL	,	NULL	,	NULL	,	NULL	,	13006	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1007	,	407	,	30087	,	50087	,	55087	,	NULL	,	NULL	,	NULL	,	NULL	,	13007	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1008	,	408	,	30001	,	50001	,	55001	,	NULL	,	NULL	,	NULL	,	NULL	,	13008	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1009	,	409	,	30163	,	50163	,	55163	,	NULL	,	NULL	,	NULL	,	NULL	,	13009	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1010	,	410	,	30255	,	50255	,	55255	,	NULL	,	NULL	,	NULL	,	NULL	,	13010	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1011	,	411	,	30263	,	50263	,	55263	,	NULL	,	NULL	,	NULL	,	NULL	,	13011	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1012	,	412	,	30264	,	50264	,	55264	,	NULL	,	NULL	,	NULL	,	NULL	,	13012	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1013	,	413	,	30265	,	50265	,	55265	,	NULL	,	NULL	,	NULL	,	NULL	,	13013	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1014	,	414	,	30266	,	50266	,	55266	,	NULL	,	NULL	,	NULL	,	NULL	,	13014	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1015	,	415	,	30267	,	50267	,	55267	,	NULL	,	NULL	,	NULL	,	NULL	,	13015	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1016	,	416	,	30262	,	50262	,	55262	,	NULL	,	NULL	,	NULL	,	NULL	,	13016	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1017	,	417	,	30268	,	50268	,	55268	,	NULL	,	NULL	,	NULL	,	NULL	,	13017	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1018	,	418	,	30269	,	50269	,	55269	,	NULL	,	NULL	,	NULL	,	NULL	,	13018	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1019	,	419	,	30270	,	50270	,	55270	,	NULL	,	NULL	,	NULL	,	NULL	,	13019	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1020	,	420	,	30271	,	50271	,	55271	,	NULL	,	NULL	,	NULL	,	NULL	,	13020	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1021	,	421	,	30272	,	50272	,	55272	,	NULL	,	NULL	,	NULL	,	NULL	,	13021	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1022	,	422	,	30276	,	50276	,	55276	,	NULL	,	NULL	,	NULL	,	NULL	,	13022	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1023	,	423	,	30277	,	50277	,	55277	,	NULL	,	NULL	,	NULL	,	NULL	,	13023	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1024	,	424	,	30283	,	50283	,	55283	,	NULL	,	NULL	,	NULL	,	NULL	,	13024	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1025	,	425	,	30292	,	50292	,	55292	,	NULL	,	NULL	,	NULL	,	NULL	,	13025	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1026	,	426	,	30293	,	50293	,	55293	,	NULL	,	NULL	,	NULL	,	NULL	,	13026	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1027	,	427	,	30294	,	50294	,	55294	,	NULL	,	NULL	,	NULL	,	NULL	,	13027	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1028	,	428	,	30295	,	50295	,	55295	,	NULL	,	NULL	,	NULL	,	NULL	,	13028	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1029	,	429	,	30318	,	50318	,	55318	,	NULL	,	NULL	,	NULL	,	NULL	,	13029	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1030	,	430	,	30337	,	50337	,	55337	,	NULL	,	NULL	,	NULL	,	NULL	,	13030	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1031	,	431	,	30000	,	50000	,	55000	,	NULL	,	NULL	,	NULL	,	NULL	,	13031	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1032	,	432	,	30002	,	50002	,	55002	,	NULL	,	NULL	,	NULL	,	NULL	,	13032	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1033	,	433	,	30003	,	50003	,	55003	,	NULL	,	NULL	,	NULL	,	NULL	,	13033	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1034	,	434	,	30004	,	50004	,	55004	,	NULL	,	NULL	,	NULL	,	NULL	,	13034	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1035	,	435	,	30005	,	50005	,	55005	,	NULL	,	NULL	,	NULL	,	NULL	,	13035	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1036	,	436	,	30006	,	50006	,	55006	,	NULL	,	NULL	,	NULL	,	NULL	,	13036	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1037	,	437	,	30007	,	50007	,	55007	,	NULL	,	NULL	,	NULL	,	NULL	,	13037	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1038	,	438	,	30008	,	50008	,	55008	,	NULL	,	NULL	,	NULL	,	NULL	,	13038	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1039	,	439	,	30009	,	50009	,	55009	,	NULL	,	NULL	,	NULL	,	NULL	,	13039	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1040	,	440	,	30010	,	50010	,	55010	,	NULL	,	NULL	,	NULL	,	NULL	,	13040	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1041	,	441	,	30011	,	50011	,	55011	,	NULL	,	NULL	,	NULL	,	NULL	,	13041	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1042	,	442	,	30012	,	50012	,	55012	,	NULL	,	NULL	,	NULL	,	NULL	,	13042	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1043	,	443	,	30013	,	50013	,	55013	,	NULL	,	NULL	,	NULL	,	NULL	,	13043	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1044	,	444	,	30015	,	50015	,	55015	,	NULL	,	NULL	,	NULL	,	NULL	,	13044	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1045	,	445	,	30016	,	50016	,	55016	,	NULL	,	NULL	,	NULL	,	NULL	,	13045	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1046	,	446	,	30017	,	50017	,	55017	,	NULL	,	NULL	,	NULL	,	NULL	,	13046	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1047	,	447	,	30018	,	50018	,	55018	,	NULL	,	NULL	,	NULL	,	NULL	,	13047	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1048	,	448	,	30020	,	50020	,	55020	,	NULL	,	NULL	,	NULL	,	NULL	,	13048	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1049	,	449	,	30021	,	50021	,	55021	,	NULL	,	NULL	,	NULL	,	NULL	,	13049	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1050	,	450	,	30022	,	50022	,	55022	,	NULL	,	NULL	,	NULL	,	NULL	,	13050	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1051	,	451	,	30023	,	50023	,	55023	,	NULL	,	NULL	,	NULL	,	NULL	,	13051	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1052	,	452	,	30024	,	50024	,	55024	,	NULL	,	NULL	,	NULL	,	NULL	,	13052	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1053	,	453	,	30025	,	50025	,	55025	,	NULL	,	NULL	,	NULL	,	NULL	,	13053	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1054	,	454	,	30026	,	50026	,	55026	,	NULL	,	NULL	,	NULL	,	NULL	,	13054	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1055	,	455	,	30027	,	50027	,	55027	,	NULL	,	NULL	,	NULL	,	NULL	,	13055	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1056	,	456	,	30028	,	50028	,	55028	,	NULL	,	NULL	,	NULL	,	NULL	,	13056	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1057	,	457	,	30030	,	50030	,	55030	,	NULL	,	NULL	,	NULL	,	NULL	,	13057	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1058	,	458	,	30031	,	50031	,	55031	,	NULL	,	NULL	,	NULL	,	NULL	,	13058	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1059	,	459	,	30032	,	50032	,	55032	,	NULL	,	NULL	,	NULL	,	NULL	,	13059	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1060	,	460	,	30033	,	50033	,	55033	,	NULL	,	NULL	,	NULL	,	NULL	,	13060	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1061	,	461	,	30034	,	50034	,	55034	,	NULL	,	NULL	,	NULL	,	NULL	,	13061	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1062	,	462	,	30035	,	50035	,	55035	,	NULL	,	NULL	,	NULL	,	NULL	,	13062	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1063	,	463	,	30036	,	50036	,	55036	,	NULL	,	NULL	,	NULL	,	NULL	,	13063	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1064	,	464	,	30037	,	50037	,	55037	,	NULL	,	NULL	,	NULL	,	NULL	,	13064	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1065	,	465	,	30038	,	50038	,	55038	,	NULL	,	NULL	,	NULL	,	NULL	,	13065	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1066	,	466	,	30039	,	50039	,	55039	,	NULL	,	NULL	,	NULL	,	NULL	,	13066	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1067	,	467	,	30040	,	50040	,	55040	,	NULL	,	NULL	,	NULL	,	NULL	,	13067	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1068	,	468	,	30041	,	50041	,	55041	,	NULL	,	NULL	,	NULL	,	NULL	,	13068	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1069	,	469	,	30042	,	50042	,	55042	,	NULL	,	NULL	,	NULL	,	NULL	,	13069	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1070	,	470	,	30043	,	50043	,	55043	,	NULL	,	NULL	,	NULL	,	NULL	,	13070	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1071	,	471	,	30071	,	50071	,	55071	,	NULL	,	NULL	,	NULL	,	NULL	,	13071	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1072	,	472	,	30072	,	50072	,	55072	,	NULL	,	NULL	,	NULL	,	NULL	,	13072	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1073	,	473	,	30074	,	50074	,	55074	,	NULL	,	NULL	,	NULL	,	NULL	,	13073	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1074	,	474	,	30075	,	50075	,	55075	,	NULL	,	NULL	,	NULL	,	NULL	,	13074	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1075	,	475	,	30076	,	50076	,	55076	,	NULL	,	NULL	,	NULL	,	NULL	,	13075	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1076	,	476	,	30077	,	50077	,	55077	,	NULL	,	NULL	,	NULL	,	NULL	,	13076	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1077	,	477	,	30078	,	50078	,	55078	,	NULL	,	NULL	,	NULL	,	NULL	,	13077	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1078	,	478	,	30079	,	50079	,	55079	,	NULL	,	NULL	,	NULL	,	NULL	,	13078	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1079	,	479	,	30080	,	50080	,	55080	,	NULL	,	NULL	,	NULL	,	NULL	,	13079	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1080	,	480	,	30044	,	50044	,	55044	,	NULL	,	NULL	,	NULL	,	NULL	,	13080	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1081	,	481	,	30045	,	50045	,	55045	,	NULL	,	NULL	,	NULL	,	NULL	,	13081	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1082	,	482	,	30046	,	50046	,	55046	,	NULL	,	NULL	,	NULL	,	NULL	,	13082	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1083	,	483	,	30047	,	50047	,	55047	,	NULL	,	NULL	,	NULL	,	NULL	,	13083	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1084	,	484	,	30048	,	50048	,	55048	,	NULL	,	NULL	,	NULL	,	NULL	,	13084	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1085	,	485	,	30049	,	50049	,	55049	,	NULL	,	NULL	,	NULL	,	NULL	,	13085	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1086	,	486	,	30050	,	50050	,	55050	,	NULL	,	NULL	,	NULL	,	NULL	,	13086	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1087	,	487	,	30051	,	50051	,	55051	,	NULL	,	NULL	,	NULL	,	NULL	,	13087	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1088	,	488	,	30052	,	50052	,	55052	,	NULL	,	NULL	,	NULL	,	NULL	,	13088	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1089	,	489	,	30053	,	50053	,	55053	,	NULL	,	NULL	,	NULL	,	NULL	,	13089	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1090	,	490	,	30055	,	50055	,	55055	,	NULL	,	NULL	,	NULL	,	NULL	,	13090	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1091	,	491	,	30056	,	50056	,	55056	,	NULL	,	NULL	,	NULL	,	NULL	,	13091	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1092	,	492	,	30057	,	50057	,	55057	,	NULL	,	NULL	,	NULL	,	NULL	,	13092	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1093	,	493	,	30058	,	50058	,	55058	,	NULL	,	NULL	,	NULL	,	NULL	,	13093	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1094	,	494	,	30060	,	50060	,	55060	,	NULL	,	NULL	,	NULL	,	NULL	,	13094	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1095	,	495	,	30061	,	50061	,	55061	,	NULL	,	NULL	,	NULL	,	NULL	,	13095	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1096	,	496	,	30062	,	50062	,	55062	,	NULL	,	NULL	,	NULL	,	NULL	,	13096	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1097	,	497	,	30063	,	50063	,	55063	,	NULL	,	NULL	,	NULL	,	NULL	,	13097	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1098	,	498	,	30064	,	50064	,	55064	,	NULL	,	NULL	,	NULL	,	NULL	,	13098	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1099	,	499	,	30065	,	50065	,	55065	,	NULL	,	NULL	,	NULL	,	NULL	,	13099	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1100	,	500	,	30066	,	50066	,	55066	,	NULL	,	NULL	,	NULL	,	NULL	,	13100	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1101	,	501	,	30067	,	50067	,	55067	,	NULL	,	NULL	,	NULL	,	NULL	,	13101	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1102	,	502	,	30068	,	50068	,	55068	,	NULL	,	NULL	,	NULL	,	NULL	,	13102	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1103	,	503	,	30069	,	50069	,	55069	,	NULL	,	NULL	,	NULL	,	NULL	,	13103	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1104	,	504	,	30070	,	50070	,	55070	,	NULL	,	NULL	,	NULL	,	NULL	,	13104	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1105	,	505	,	30081	,	50081	,	55081	,	NULL	,	NULL	,	NULL	,	NULL	,	13105	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1106	,	506	,	30082	,	50082	,	55082	,	NULL	,	NULL	,	NULL	,	NULL	,	13106	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1107	,	507	,	30083	,	50083	,	55083	,	NULL	,	NULL	,	NULL	,	NULL	,	13107	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1108	,	508	,	30085	,	50085	,	55085	,	NULL	,	NULL	,	NULL	,	NULL	,	13108	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1109	,	509	,	30086	,	50086	,	55086	,	NULL	,	NULL	,	NULL	,	NULL	,	13109	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1110	,	510	,	30088	,	50088	,	55088	,	NULL	,	NULL	,	NULL	,	NULL	,	13110	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1111	,	511	,	30089	,	50089	,	55089	,	NULL	,	NULL	,	NULL	,	NULL	,	13111	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1112	,	512	,	30090	,	50090	,	55090	,	NULL	,	NULL	,	NULL	,	NULL	,	13112	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1113	,	513	,	30091	,	50091	,	55091	,	NULL	,	NULL	,	NULL	,	NULL	,	13113	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1114	,	514	,	30092	,	50092	,	55092	,	NULL	,	NULL	,	NULL	,	NULL	,	13114	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1115	,	515	,	30093	,	50093	,	55093	,	NULL	,	NULL	,	NULL	,	NULL	,	13115	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1116	,	516	,	30094	,	50094	,	55094	,	NULL	,	NULL	,	NULL	,	NULL	,	13116	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1117	,	517	,	30095	,	50095	,	55095	,	NULL	,	NULL	,	NULL	,	NULL	,	13117	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1118	,	518	,	30096	,	50096	,	55096	,	NULL	,	NULL	,	NULL	,	NULL	,	13118	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1119	,	519	,	30097	,	50097	,	55097	,	NULL	,	NULL	,	NULL	,	NULL	,	13119	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1120	,	520	,	30098	,	50098	,	55098	,	NULL	,	NULL	,	NULL	,	NULL	,	13120	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1121	,	521	,	30099	,	50099	,	55099	,	NULL	,	NULL	,	NULL	,	NULL	,	13121	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1122	,	522	,	30100	,	50100	,	55100	,	NULL	,	NULL	,	NULL	,	NULL	,	13122	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1123	,	523	,	30101	,	50101	,	55101	,	NULL	,	NULL	,	NULL	,	NULL	,	13123	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1124	,	524	,	30102	,	50102	,	55102	,	NULL	,	NULL	,	NULL	,	NULL	,	13124	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1125	,	525	,	30103	,	50103	,	55103	,	NULL	,	NULL	,	NULL	,	NULL	,	13125	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1126	,	526	,	30104	,	50104	,	55104	,	NULL	,	NULL	,	NULL	,	NULL	,	13126	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1127	,	527	,	30105	,	50105	,	55105	,	NULL	,	NULL	,	NULL	,	NULL	,	13127	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1128	,	528	,	30106	,	50106	,	55106	,	NULL	,	NULL	,	NULL	,	NULL	,	13128	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1129	,	529	,	30107	,	50107	,	55107	,	NULL	,	NULL	,	NULL	,	NULL	,	13129	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1130	,	530	,	30108	,	50108	,	55108	,	NULL	,	NULL	,	NULL	,	NULL	,	13130	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1131	,	531	,	30109	,	50109	,	55109	,	NULL	,	NULL	,	NULL	,	NULL	,	13131	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1132	,	532	,	30110	,	50110	,	55110	,	NULL	,	NULL	,	NULL	,	NULL	,	13132	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1133	,	533	,	30111	,	50111	,	55111	,	NULL	,	NULL	,	NULL	,	NULL	,	13133	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1134	,	534	,	30112	,	50112	,	55112	,	NULL	,	NULL	,	NULL	,	NULL	,	13134	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1135	,	535	,	30113	,	50113	,	55113	,	NULL	,	NULL	,	NULL	,	NULL	,	13135	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1136	,	536	,	30114	,	50114	,	55114	,	NULL	,	NULL	,	NULL	,	NULL	,	13136	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1137	,	537	,	30115	,	50115	,	55115	,	NULL	,	NULL	,	NULL	,	NULL	,	13137	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1138	,	538	,	30116	,	50116	,	55116	,	NULL	,	NULL	,	NULL	,	NULL	,	13138	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1139	,	539	,	30117	,	50117	,	55117	,	NULL	,	NULL	,	NULL	,	NULL	,	13139	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1140	,	540	,	30118	,	50118	,	55118	,	NULL	,	NULL	,	NULL	,	NULL	,	13140	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1141	,	541	,	30119	,	50119	,	55119	,	NULL	,	NULL	,	NULL	,	NULL	,	13141	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1142	,	542	,	30120	,	50120	,	55120	,	NULL	,	NULL	,	NULL	,	NULL	,	13142	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1143	,	543	,	30121	,	50121	,	55121	,	NULL	,	NULL	,	NULL	,	NULL	,	13143	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1144	,	544	,	30122	,	50122	,	55122	,	NULL	,	NULL	,	NULL	,	NULL	,	13144	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1145	,	545	,	30123	,	50123	,	55123	,	NULL	,	NULL	,	NULL	,	NULL	,	13145	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1146	,	546	,	30124	,	50124	,	55124	,	NULL	,	NULL	,	NULL	,	NULL	,	13146	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1147	,	547	,	30125	,	50125	,	55125	,	NULL	,	NULL	,	NULL	,	NULL	,	13147	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1148	,	548	,	30126	,	50126	,	55126	,	NULL	,	NULL	,	NULL	,	NULL	,	13148	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1149	,	549	,	30127	,	50127	,	55127	,	NULL	,	NULL	,	NULL	,	NULL	,	13149	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1150	,	550	,	30128	,	50128	,	55128	,	NULL	,	NULL	,	NULL	,	NULL	,	13150	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1151	,	551	,	30129	,	50129	,	55129	,	NULL	,	NULL	,	NULL	,	NULL	,	13151	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1152	,	552	,	30130	,	50130	,	55130	,	NULL	,	NULL	,	NULL	,	NULL	,	13152	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1153	,	553	,	30131	,	50131	,	55131	,	NULL	,	NULL	,	NULL	,	NULL	,	13153	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1154	,	554	,	30132	,	50132	,	55132	,	NULL	,	NULL	,	NULL	,	NULL	,	13154	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1155	,	555	,	30133	,	50133	,	55133	,	NULL	,	NULL	,	NULL	,	NULL	,	13155	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1156	,	556	,	30134	,	50134	,	55134	,	NULL	,	NULL	,	NULL	,	NULL	,	13156	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1157	,	557	,	30135	,	50135	,	55135	,	NULL	,	NULL	,	NULL	,	NULL	,	13157	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1158	,	558	,	30136	,	50136	,	55136	,	NULL	,	NULL	,	NULL	,	NULL	,	13158	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1159	,	559	,	30137	,	50137	,	55137	,	NULL	,	NULL	,	NULL	,	NULL	,	13159	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1160	,	560	,	30138	,	50138	,	55138	,	NULL	,	NULL	,	NULL	,	NULL	,	13160	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1161	,	561	,	30139	,	50139	,	55139	,	NULL	,	NULL	,	NULL	,	NULL	,	13161	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1162	,	562	,	30140	,	50140	,	55140	,	NULL	,	NULL	,	NULL	,	NULL	,	13162	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1163	,	563	,	30141	,	50141	,	55141	,	NULL	,	NULL	,	NULL	,	NULL	,	13163	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1164	,	564	,	30142	,	50142	,	55142	,	NULL	,	NULL	,	NULL	,	NULL	,	13164	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1165	,	565	,	30143	,	50143	,	55143	,	NULL	,	NULL	,	NULL	,	NULL	,	13165	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1166	,	566	,	30191	,	50191	,	55191	,	NULL	,	NULL	,	NULL	,	NULL	,	13166	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1167	,	567	,	30192	,	50192	,	55192	,	NULL	,	NULL	,	NULL	,	NULL	,	13167	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1168	,	568	,	30193	,	50193	,	55193	,	NULL	,	NULL	,	NULL	,	NULL	,	13168	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1169	,	569	,	30194	,	50194	,	55194	,	NULL	,	NULL	,	NULL	,	NULL	,	13169	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1170	,	570	,	30195	,	50195	,	55195	,	NULL	,	NULL	,	NULL	,	NULL	,	13170	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1171	,	571	,	30196	,	50196	,	55196	,	NULL	,	NULL	,	NULL	,	NULL	,	13171	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1172	,	572	,	30197	,	50197	,	55197	,	NULL	,	NULL	,	NULL	,	NULL	,	13172	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1173	,	573	,	30177	,	50177	,	55177	,	NULL	,	NULL	,	NULL	,	NULL	,	13173	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1174	,	574	,	30178	,	50178	,	55178	,	NULL	,	NULL	,	NULL	,	NULL	,	13174	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1175	,	575	,	30179	,	50179	,	55179	,	NULL	,	NULL	,	NULL	,	NULL	,	13175	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1176	,	576	,	30180	,	50180	,	55180	,	NULL	,	NULL	,	NULL	,	NULL	,	13176	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1177	,	577	,	30181	,	50181	,	55181	,	NULL	,	NULL	,	NULL	,	NULL	,	13177	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1178	,	578	,	30182	,	50182	,	55182	,	NULL	,	NULL	,	NULL	,	NULL	,	13178	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1179	,	579	,	30183	,	50183	,	55183	,	NULL	,	NULL	,	NULL	,	NULL	,	13179	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1180	,	580	,	30184	,	50184	,	55184	,	NULL	,	NULL	,	NULL	,	NULL	,	13180	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1181	,	581	,	30185	,	50185	,	55185	,	NULL	,	NULL	,	NULL	,	NULL	,	13181	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1182	,	582	,	30186	,	50186	,	55186	,	NULL	,	NULL	,	NULL	,	NULL	,	13182	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1183	,	583	,	30187	,	50187	,	55187	,	NULL	,	NULL	,	NULL	,	NULL	,	13183	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1184	,	584	,	30188	,	50188	,	55188	,	NULL	,	NULL	,	NULL	,	NULL	,	13184	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1185	,	585	,	30189	,	50189	,	55189	,	NULL	,	NULL	,	NULL	,	NULL	,	13185	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1186	,	586	,	30190	,	50190	,	55190	,	NULL	,	NULL	,	NULL	,	NULL	,	13186	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1187	,	587	,	30165	,	50165	,	55165	,	NULL	,	NULL	,	NULL	,	NULL	,	13187	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1188	,	588	,	30166	,	50166	,	55166	,	NULL	,	NULL	,	NULL	,	NULL	,	13188	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1189	,	589	,	30167	,	50167	,	55167	,	NULL	,	NULL	,	NULL	,	NULL	,	13189	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1190	,	590	,	30168	,	50168	,	55168	,	NULL	,	NULL	,	NULL	,	NULL	,	13190	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1191	,	591	,	30169	,	50169	,	55169	,	NULL	,	NULL	,	NULL	,	NULL	,	13191	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1192	,	592	,	30170	,	50170	,	55170	,	NULL	,	NULL	,	NULL	,	NULL	,	13192	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1193	,	593	,	30171	,	50171	,	55171	,	NULL	,	NULL	,	NULL	,	NULL	,	13193	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1194	,	594	,	30172	,	50172	,	55172	,	NULL	,	NULL	,	NULL	,	NULL	,	13194	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1195	,	595	,	30144	,	50144	,	55144	,	NULL	,	NULL	,	NULL	,	NULL	,	13195	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1196	,	596	,	30145	,	50145	,	55145	,	NULL	,	NULL	,	NULL	,	NULL	,	13196	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1197	,	597	,	30146	,	50146	,	55146	,	NULL	,	NULL	,	NULL	,	NULL	,	13197	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1198	,	598	,	30147	,	50147	,	55147	,	NULL	,	NULL	,	NULL	,	NULL	,	13198	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1199	,	599	,	30148	,	50148	,	55148	,	NULL	,	NULL	,	NULL	,	NULL	,	13199	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1200	,	600	,	30149	,	50149	,	55149	,	NULL	,	NULL	,	NULL	,	NULL	,	13200	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1201	,	601	,	30150	,	50150	,	55150	,	NULL	,	NULL	,	NULL	,	NULL	,	13201	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1202	,	602	,	30151	,	50151	,	55151	,	NULL	,	NULL	,	NULL	,	NULL	,	13202	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1203	,	603	,	30152	,	50152	,	55152	,	NULL	,	NULL	,	NULL	,	NULL	,	13203	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1204	,	604	,	30153	,	50153	,	55153	,	NULL	,	NULL	,	NULL	,	NULL	,	13204	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1205	,	605	,	30154	,	50154	,	55154	,	NULL	,	NULL	,	NULL	,	NULL	,	13205	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1206	,	606	,	30155	,	50155	,	55155	,	NULL	,	NULL	,	NULL	,	NULL	,	13206	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1207	,	607	,	30156	,	50156	,	55156	,	NULL	,	NULL	,	NULL	,	NULL	,	13207	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1208	,	608	,	30157	,	50157	,	55157	,	NULL	,	NULL	,	NULL	,	NULL	,	13208	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1209	,	609	,	30158	,	50158	,	55158	,	NULL	,	NULL	,	NULL	,	NULL	,	13209	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1210	,	610	,	30159	,	50159	,	55159	,	NULL	,	NULL	,	NULL	,	NULL	,	13210	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1211	,	611	,	30160	,	50160	,	55160	,	NULL	,	NULL	,	NULL	,	NULL	,	13211	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1212	,	612	,	30161	,	50161	,	55161	,	NULL	,	NULL	,	NULL	,	NULL	,	13212	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1213	,	613	,	30162	,	50162	,	55162	,	NULL	,	NULL	,	NULL	,	NULL	,	13213	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1214	,	614	,	30164	,	50164	,	55164	,	NULL	,	NULL	,	NULL	,	NULL	,	13214	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1215	,	615	,	30173	,	50173	,	55173	,	NULL	,	NULL	,	NULL	,	NULL	,	13215	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1216	,	616	,	30174	,	50174	,	55174	,	NULL	,	NULL	,	NULL	,	NULL	,	13216	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1217	,	617	,	30175	,	50175	,	55175	,	NULL	,	NULL	,	NULL	,	NULL	,	13217	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1218	,	618	,	30176	,	50176	,	55176	,	NULL	,	NULL	,	NULL	,	NULL	,	13218	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1219	,	619	,	30198	,	50198	,	55198	,	NULL	,	NULL	,	NULL	,	NULL	,	13219	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1220	,	620	,	30199	,	50199	,	55199	,	NULL	,	NULL	,	NULL	,	NULL	,	13220	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1221	,	621	,	30200	,	50200	,	55200	,	NULL	,	NULL	,	NULL	,	NULL	,	13221	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1222	,	622	,	30201	,	50201	,	55201	,	NULL	,	NULL	,	NULL	,	NULL	,	13222	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1223	,	623	,	30202	,	50202	,	55202	,	NULL	,	NULL	,	NULL	,	NULL	,	13223	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1224	,	624	,	30203	,	50203	,	55203	,	NULL	,	NULL	,	NULL	,	NULL	,	13224	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1225	,	625	,	30204	,	50204	,	55204	,	NULL	,	NULL	,	NULL	,	NULL	,	13225	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1226	,	626	,	30205	,	50205	,	55205	,	NULL	,	NULL	,	NULL	,	NULL	,	13226	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1227	,	627	,	30206	,	50206	,	55206	,	NULL	,	NULL	,	NULL	,	NULL	,	13227	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1228	,	628	,	30207	,	50207	,	55207	,	NULL	,	NULL	,	NULL	,	NULL	,	13228	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1229	,	629	,	30208	,	50208	,	55208	,	NULL	,	NULL	,	NULL	,	NULL	,	13229	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1230	,	630	,	30209	,	50209	,	55209	,	NULL	,	NULL	,	NULL	,	NULL	,	13230	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1231	,	631	,	30210	,	50210	,	55210	,	NULL	,	NULL	,	NULL	,	NULL	,	13231	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1232	,	632	,	30211	,	50211	,	55211	,	NULL	,	NULL	,	NULL	,	NULL	,	13232	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1233	,	633	,	30212	,	50212	,	55212	,	NULL	,	NULL	,	NULL	,	NULL	,	13233	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1234	,	634	,	30213	,	50213	,	55213	,	NULL	,	NULL	,	NULL	,	NULL	,	13234	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1235	,	635	,	30214	,	50214	,	55214	,	NULL	,	NULL	,	NULL	,	NULL	,	13235	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1236	,	636	,	30215	,	50215	,	55215	,	NULL	,	NULL	,	NULL	,	NULL	,	13236	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1237	,	637	,	30216	,	50216	,	55216	,	NULL	,	NULL	,	NULL	,	NULL	,	13237	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1238	,	638	,	30217	,	50217	,	55217	,	NULL	,	NULL	,	NULL	,	NULL	,	13238	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1239	,	639	,	30218	,	50218	,	55218	,	NULL	,	NULL	,	NULL	,	NULL	,	13239	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1240	,	640	,	30219	,	50219	,	55219	,	NULL	,	NULL	,	NULL	,	NULL	,	13240	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1241	,	641	,	30220	,	50220	,	55220	,	NULL	,	NULL	,	NULL	,	NULL	,	13241	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1242	,	642	,	30221	,	50221	,	55221	,	NULL	,	NULL	,	NULL	,	NULL	,	13242	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1243	,	643	,	30222	,	50222	,	55222	,	NULL	,	NULL	,	NULL	,	NULL	,	13243	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1244	,	644	,	30223	,	50223	,	55223	,	NULL	,	NULL	,	NULL	,	NULL	,	13244	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1245	,	645	,	30224	,	50224	,	55224	,	NULL	,	NULL	,	NULL	,	NULL	,	13245	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1246	,	646	,	30225	,	50225	,	55225	,	NULL	,	NULL	,	NULL	,	NULL	,	13246	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1247	,	647	,	30226	,	50226	,	55226	,	NULL	,	NULL	,	NULL	,	NULL	,	13247	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1248	,	648	,	30227	,	50227	,	55227	,	NULL	,	NULL	,	NULL	,	NULL	,	13248	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1249	,	649	,	30228	,	50228	,	55228	,	NULL	,	NULL	,	NULL	,	NULL	,	13249	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1250	,	650	,	30229	,	50229	,	55229	,	NULL	,	NULL	,	NULL	,	NULL	,	13250	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1251	,	651	,	30249	,	50249	,	55249	,	NULL	,	NULL	,	NULL	,	NULL	,	13251	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1252	,	652	,	30250	,	50250	,	55250	,	NULL	,	NULL	,	NULL	,	NULL	,	13252	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1253	,	653	,	30251	,	50251	,	55251	,	NULL	,	NULL	,	NULL	,	NULL	,	13253	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1254	,	654	,	30252	,	50252	,	55252	,	NULL	,	NULL	,	NULL	,	NULL	,	13254	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1255	,	655	,	30253	,	50253	,	55253	,	NULL	,	NULL	,	NULL	,	NULL	,	13255	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1256	,	656	,	30254	,	50254	,	55254	,	NULL	,	NULL	,	NULL	,	NULL	,	13256	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1257	,	657	,	30256	,	50256	,	55256	,	NULL	,	NULL	,	NULL	,	NULL	,	13257	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1258	,	658	,	30257	,	50257	,	55257	,	NULL	,	NULL	,	NULL	,	NULL	,	13258	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1259	,	659	,	30258	,	50258	,	55258	,	NULL	,	NULL	,	NULL	,	NULL	,	13259	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1260	,	660	,	30259	,	50259	,	55259	,	NULL	,	NULL	,	NULL	,	NULL	,	13260	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1261	,	661	,	30260	,	50260	,	55260	,	NULL	,	NULL	,	NULL	,	NULL	,	13261	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1262	,	662	,	30261	,	50261	,	55261	,	NULL	,	NULL	,	NULL	,	NULL	,	13262	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1263	,	663	,	30338	,	50338	,	55338	,	NULL	,	NULL	,	NULL	,	NULL	,	13263	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1264	,	664	,	30339	,	50339	,	55339	,	NULL	,	NULL	,	NULL	,	NULL	,	13264	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1265	,	665	,	30340	,	50340	,	55340	,	NULL	,	NULL	,	NULL	,	NULL	,	13265	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1266	,	666	,	30341	,	50341	,	55341	,	NULL	,	NULL	,	NULL	,	NULL	,	13266	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1267	,	667	,	30342	,	50342	,	55342	,	NULL	,	NULL	,	NULL	,	NULL	,	13267	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1268	,	668	,	30343	,	50343	,	55343	,	NULL	,	NULL	,	NULL	,	NULL	,	13268	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1269	,	669	,	30344	,	50344	,	55344	,	NULL	,	NULL	,	NULL	,	NULL	,	13269	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1270	,	670	,	30345	,	50345	,	55345	,	NULL	,	NULL	,	NULL	,	NULL	,	13270	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1271	,	671	,	30346	,	50346	,	55346	,	NULL	,	NULL	,	NULL	,	NULL	,	13271	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1272	,	672	,	30347	,	50347	,	55347	,	NULL	,	NULL	,	NULL	,	NULL	,	13272	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1273	,	673	,	30242	,	50242	,	55242	,	NULL	,	NULL	,	NULL	,	NULL	,	13273	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1274	,	674	,	30243	,	50243	,	55243	,	NULL	,	NULL	,	NULL	,	NULL	,	13274	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1275	,	675	,	30244	,	50244	,	55244	,	NULL	,	NULL	,	NULL	,	NULL	,	13275	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1276	,	676	,	30245	,	50245	,	55245	,	NULL	,	NULL	,	NULL	,	NULL	,	13276	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1277	,	677	,	30246	,	50246	,	55246	,	NULL	,	NULL	,	NULL	,	NULL	,	13277	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1278	,	678	,	30247	,	50247	,	55247	,	NULL	,	NULL	,	NULL	,	NULL	,	13278	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1279	,	679	,	30248	,	50248	,	55248	,	NULL	,	NULL	,	NULL	,	NULL	,	13279	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1280	,	680	,	30230	,	50230	,	55230	,	NULL	,	NULL	,	NULL	,	NULL	,	13280	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1281	,	681	,	30231	,	50231	,	55231	,	NULL	,	NULL	,	NULL	,	NULL	,	13281	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1282	,	682	,	30232	,	50232	,	55232	,	NULL	,	NULL	,	NULL	,	NULL	,	13282	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1283	,	683	,	30233	,	50233	,	55233	,	NULL	,	NULL	,	NULL	,	NULL	,	13283	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1284	,	684	,	30234	,	50234	,	55234	,	NULL	,	NULL	,	NULL	,	NULL	,	13284	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1285	,	685	,	30235	,	50235	,	55235	,	NULL	,	NULL	,	NULL	,	NULL	,	13285	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1286	,	686	,	30236	,	50236	,	55236	,	NULL	,	NULL	,	NULL	,	NULL	,	13286	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1287	,	687	,	30237	,	50237	,	55237	,	NULL	,	NULL	,	NULL	,	NULL	,	13287	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1288	,	688	,	30238	,	50238	,	55238	,	NULL	,	NULL	,	NULL	,	NULL	,	13288	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1289	,	689	,	30239	,	50239	,	55239	,	NULL	,	NULL	,	NULL	,	NULL	,	13289	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1290	,	690	,	30240	,	50240	,	55240	,	NULL	,	NULL	,	NULL	,	NULL	,	13290	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1291	,	691	,	30241	,	50241	,	55241	,	NULL	,	NULL	,	NULL	,	NULL	,	13291	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1292	,	692	,	30273	,	50273	,	55273	,	NULL	,	NULL	,	NULL	,	NULL	,	13292	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1293	,	693	,	30274	,	50274	,	55274	,	NULL	,	NULL	,	NULL	,	NULL	,	13293	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1294	,	694	,	30275	,	50275	,	55275	,	NULL	,	NULL	,	NULL	,	NULL	,	13294	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1295	,	695	,	30278	,	50278	,	55278	,	NULL	,	NULL	,	NULL	,	NULL	,	13295	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1296	,	696	,	30279	,	50279	,	55279	,	NULL	,	NULL	,	NULL	,	NULL	,	13296	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1297	,	697	,	30280	,	50280	,	55280	,	NULL	,	NULL	,	NULL	,	NULL	,	13297	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1298	,	698	,	30281	,	50281	,	55281	,	NULL	,	NULL	,	NULL	,	NULL	,	13298	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1299	,	699	,	30282	,	50282	,	55282	,	NULL	,	NULL	,	NULL	,	NULL	,	13299	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1300	,	700	,	30308	,	50308	,	55308	,	NULL	,	NULL	,	NULL	,	NULL	,	13300	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1301	,	701	,	30309	,	50309	,	55309	,	NULL	,	NULL	,	NULL	,	NULL	,	13301	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1302	,	702	,	30310	,	50310	,	55310	,	NULL	,	NULL	,	NULL	,	NULL	,	13302	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1303	,	703	,	30311	,	50311	,	55311	,	NULL	,	NULL	,	NULL	,	NULL	,	13303	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1304	,	704	,	30312	,	50312	,	55312	,	NULL	,	NULL	,	NULL	,	NULL	,	13304	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1305	,	705	,	30313	,	50313	,	55313	,	NULL	,	NULL	,	NULL	,	NULL	,	13305	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1306	,	706	,	30314	,	50314	,	55314	,	NULL	,	NULL	,	NULL	,	NULL	,	13306	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1307	,	707	,	30315	,	50315	,	55315	,	NULL	,	NULL	,	NULL	,	NULL	,	13307	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1308	,	708	,	30316	,	50316	,	55316	,	NULL	,	NULL	,	NULL	,	NULL	,	13308	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1309	,	709	,	30317	,	50317	,	55317	,	NULL	,	NULL	,	NULL	,	NULL	,	13309	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1310	,	710	,	30319	,	50319	,	55319	,	NULL	,	NULL	,	NULL	,	NULL	,	13310	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1311	,	711	,	30320	,	50320	,	55320	,	NULL	,	NULL	,	NULL	,	NULL	,	13311	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1312	,	712	,	30321	,	50321	,	55321	,	NULL	,	NULL	,	NULL	,	NULL	,	13312	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1313	,	713	,	30322	,	50322	,	55322	,	NULL	,	NULL	,	NULL	,	NULL	,	13313	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1314	,	714	,	30323	,	50323	,	55323	,	NULL	,	NULL	,	NULL	,	NULL	,	13314	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1315	,	715	,	30324	,	50324	,	55324	,	NULL	,	NULL	,	NULL	,	NULL	,	13315	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1316	,	716	,	30325	,	50325	,	55325	,	NULL	,	NULL	,	NULL	,	NULL	,	13316	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1317	,	717	,	30326	,	50326	,	55326	,	NULL	,	NULL	,	NULL	,	NULL	,	13317	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1318	,	718	,	30327	,	50327	,	55327	,	NULL	,	NULL	,	NULL	,	NULL	,	13318	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1319	,	719	,	30328	,	50328	,	55328	,	NULL	,	NULL	,	NULL	,	NULL	,	13319	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1320	,	720	,	30329	,	50329	,	55329	,	NULL	,	NULL	,	NULL	,	NULL	,	13320	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1321	,	721	,	30330	,	50330	,	55330	,	NULL	,	NULL	,	NULL	,	NULL	,	13321	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1322	,	722	,	30296	,	50296	,	55296	,	NULL	,	NULL	,	NULL	,	NULL	,	13322	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1323	,	723	,	30284	,	50284	,	55284	,	NULL	,	NULL	,	NULL	,	NULL	,	13323	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1324	,	724	,	30285	,	50285	,	55285	,	NULL	,	NULL	,	NULL	,	NULL	,	13324	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1325	,	725	,	30286	,	50286	,	55286	,	NULL	,	NULL	,	NULL	,	NULL	,	13325	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1326	,	726	,	30287	,	50287	,	55287	,	NULL	,	NULL	,	NULL	,	NULL	,	13326	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1327	,	727	,	30288	,	50288	,	55288	,	NULL	,	NULL	,	NULL	,	NULL	,	13327	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1328	,	728	,	30289	,	50289	,	55289	,	NULL	,	NULL	,	NULL	,	NULL	,	13328	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1329	,	729	,	30290	,	50290	,	55290	,	NULL	,	NULL	,	NULL	,	NULL	,	13329	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1330	,	730	,	30291	,	50291	,	55291	,	NULL	,	NULL	,	NULL	,	NULL	,	13330	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1331	,	731	,	30297	,	50297	,	55297	,	NULL	,	NULL	,	NULL	,	NULL	,	13331	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1332	,	732	,	30298	,	50298	,	55298	,	NULL	,	NULL	,	NULL	,	NULL	,	13332	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1333	,	733	,	30299	,	50299	,	55299	,	NULL	,	NULL	,	NULL	,	NULL	,	13333	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1334	,	734	,	30300	,	50300	,	55300	,	NULL	,	NULL	,	NULL	,	NULL	,	13334	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1335	,	735	,	30301	,	50301	,	55301	,	NULL	,	NULL	,	NULL	,	NULL	,	13335	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1336	,	736	,	30302	,	50302	,	55302	,	NULL	,	NULL	,	NULL	,	NULL	,	13336	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1337	,	737	,	30303	,	50303	,	55303	,	NULL	,	NULL	,	NULL	,	NULL	,	13337	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1338	,	738	,	30304	,	50304	,	55304	,	NULL	,	NULL	,	NULL	,	NULL	,	13338	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1339	,	739	,	30305	,	50305	,	55305	,	NULL	,	NULL	,	NULL	,	NULL	,	13339	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1340	,	740	,	30306	,	50306	,	55306	,	NULL	,	NULL	,	NULL	,	NULL	,	13340	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1341	,	741	,	30307	,	50307	,	55307	,	NULL	,	NULL	,	NULL	,	NULL	,	13341	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1342	,	742	,	30331	,	50331	,	55331	,	NULL	,	NULL	,	NULL	,	NULL	,	13342	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1343	,	743	,	30332	,	50332	,	55332	,	NULL	,	NULL	,	NULL	,	NULL	,	13343	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1344	,	744	,	30333	,	50333	,	55333	,	NULL	,	NULL	,	NULL	,	NULL	,	13344	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1345	,	745	,	30334	,	50334	,	55334	,	NULL	,	NULL	,	NULL	,	NULL	,	13345	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1346	,	746	,	30335	,	50335	,	55335	,	NULL	,	NULL	,	NULL	,	NULL	,	13346	);
INSERT INTO antecendentes_comuna_calculado VALUES (	1347	,	747	,	30336	,	50336	,	55336	,	NULL	,	NULL	,	NULL	,	NULL	,	13347	);

CREATE TABLE tipo_comuna
(
  id_tipo_comuna serial NOT NULL,
  descripcion text NOT NULL,
  CONSTRAINT tipo_comuna_pk PRIMARY KEY (id_tipo_comuna)
)
WITH (
  OIDS=FALSE
);

INSERT INTO tipo_comuna(id_tipo_comuna, descripcion) VALUES (1, 'RURAL');
INSERT INTO tipo_comuna(id_tipo_comuna, descripcion) VALUES (2, 'COSTO FIJO');
INSERT INTO tipo_comuna(id_tipo_comuna, descripcion) VALUES (3, 'URBANA');


ALTER TABLE antecendentes_comuna ADD COLUMN tipo_comuna integer;

ALTER TABLE antecendentes_comuna
  ADD CONSTRAINT tipo_comuna_fk FOREIGN KEY (tipo_comuna)
      REFERENCES tipo_comuna (id_tipo_comuna) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


UPDATE antecendentes_comuna SET tipo_comuna=1  WHERE clasificacion = 'RURAL';
UPDATE antecendentes_comuna SET tipo_comuna=2  WHERE clasificacion = 'COSTO FIJO';
UPDATE antecendentes_comuna SET tipo_comuna=3  WHERE clasificacion = 'URBANA';

ALTER TABLE antecendentes_comuna DROP COLUMN clasificacion;
ALTER TABLE antecendentes_comuna RENAME tipo_comuna  TO clasificacion;

ALTER TABLE ano_en_curso ADD COLUMN asignacion_adulto_mayor integer;

UPDATE ano_en_curso SET asignacion_adulto_mayor=1800;

ALTER TABLE ano_en_curso
   ALTER COLUMN asignacion_adulto_mayor SET NOT NULL;

ALTER TABLE ano_en_curso ADD COLUMN inflactor numeric;

UPDATE ano_en_curso SET inflactor=1.3070;

ALTER TABLE ano_en_curso
   ALTER COLUMN inflactor SET NOT NULL;







