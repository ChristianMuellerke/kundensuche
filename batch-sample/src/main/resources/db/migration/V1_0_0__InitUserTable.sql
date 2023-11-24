drop table if exists adresses CASCADE;

drop table if exists users CASCADE;

drop table if exists users_adresses CASCADE;

drop sequence if exists hibernate_sequence;

create sequence hibernate_sequence start with 1 increment by 1;                                                                                                                                                                

create table adresses (
	id bigint not null, 
	date_created timestamp not null, 
	date_modified timestamp, 
	tenant_id varchar(30), 
	city varchar(42), 
	postalcode varchar(5), 
	street varchar(140), 
	type varchar(255), 
	primary key (id)
);
	
create table users (
	id bigint not null, 
	date_created timestamp not null, 
	date_modified timestamp, 
	tenant_id varchar(30), 
	firstname varchar(100), 
	lastname varchar(100), 
	password varchar(100), 
	primary key (id)
);              
	
create table users_adresses (
	user_id bigint not null, 
	adresses_id bigint not null, 
	primary key (user_id, 
	adresses_id)
);
	
alter table users_adresses add constraint UK_3nv9obit2stm0vrwafr32iuyh unique (adresses_id); 

alter table users_adresses add constraint FKc8ew2t9il6ck0xwbegw24dc51 foreign key (adresses_id) references adresses;

alter table users_adresses add constraint FK5oifyy7ui1fetdopc0manatr4 foreign key (user_id) references users;                                                                                                                  
                                                                                                                                                                                                                              