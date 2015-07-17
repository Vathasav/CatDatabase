# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table cat (
  id                        bigint not null,
  name                      varchar(255),
  color                     varchar(255),
  race                      varchar(255),
  gender                    integer,
  picture                   blob,
  image_file                blob,
  constraint ck_cat_gender check (gender in (0,1)),
  constraint pk_cat primary key (id))
;

create sequence cat_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists cat;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists cat_seq;

