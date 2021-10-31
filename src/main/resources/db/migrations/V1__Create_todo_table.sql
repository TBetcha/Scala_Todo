create table firstscala (
     id uuid not null primary key default uuid_generate_v4(),
     name varchar(250) not null unique,
     completed boolean not null
);
