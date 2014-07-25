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





