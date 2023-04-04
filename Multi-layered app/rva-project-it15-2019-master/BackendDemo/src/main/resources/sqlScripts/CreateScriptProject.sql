--________________________DROP________________________

drop table if exists fakultet cascade;
drop table if exists status cascade;
drop table if exists departman cascade;
drop table if exists student cascade;

drop sequence if exists seq_fakultet;
drop sequence if exists seq_status;
drop sequence if exists seq_departman;
drop sequence if exists seq_student;

--________________________CREATE________________________

create table fakultet 
(
	id integer not null,
	naziv varchar(100),
	sediste varchar(50)
);

create table status 
(
	id integer not null,
	naziv varchar(100),
	oznaka varchar(10)
);

create table departman 
(
	id integer not null,
	naziv varchar(100),
	oznaka varchar(10),
	fakultet integer not null
);

create table student 
(
	id integer not null,
	ime varchar(50),
	prezime varchar(50),
	broj_indeksa varchar(20),
	status integer not null,
	departman integer not null
);

--________________________ALTER________________________

alter table fakultet 
	add constraint pk_fakultet primary key(id);
	
alter table status
	add constraint pk_status primary key(id);
	
alter table departman
	add constraint pk_departman primary key(id);
	
alter table departman
	add constraint fk_departman_fakultet foreign key(fakultet)
		references fakultet(id) on delete cascade;

alter table student
	add constraint pk_student primary key(id);
	
alter table student
	add constraint fk_student_status foreign key(status)
		references status(id) on delete cascade;
		
alter table student
	add constraint fk_student_departman foreign key(departman)
		references departman(id) on delete cascade;

--________________________CREATE________________________

create index idx_pk_fakultet on fakultet(id);
create index idx_pk_status on status(id);
create index idx_pk_departman on departman(id);
create index idx_pk_student on student(id);
create index idx_fk_departman_fakultet on departman(fakultet);
create index idx_fk_student_status on student(status);
create index idx_fk_student_departman on student(departman);

create sequence if not exists seq_fakultet increment 1 start 1;
create sequence if not exists seq_status increment 1 start 1;
create sequence if not exists seq_departman increment 1 start 1;
create sequence if not exists seq_student increment 1 start 1;

--________________________ALTER________________________

alter table fakultet alter column id	
	set default nextval('seq_fakultet');
	
alter table status alter column id	
	set default nextval('seq_status');

alter table departman alter column id	
	set default nextval('seq_departman');
	
alter table student alter column id	
	set default nextval('seq_student');
