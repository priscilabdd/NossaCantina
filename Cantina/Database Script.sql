create table tb_usuario(
cod_usuario integer identity(1,1) not null,
user_login varchar(45) unique not null,
senha varchar(45) not null,
tipo_acesso char(1) default 0 not null,
ativo char(1) default 1 not null
);

alter table tb_usuario add constraint pk_usuario primary key (cod_usuario);

create table tb_cliente(
matricula integer not null,
nome varchar(200) not null,
email varchar(255) not null,
saldo float not null
);
alter table tb_cliente add constraint pk_cliente primary key (matricula);

create table tb_funcionario(
cod_funcionario integer identity(1,1) not null,
cpf varchar(11) unique not null,
nome varchar(200) not null,
);
alter table tb_funcionario add constraint pk_funcionario primary key (cod_funcionario);


create table tb_produto(
cod_produto integer not null,
nome varchar(200) not null,
tipo varchar(45) not null,
preco float not null
);
alter table tb_produto add constraint pk_produto primary key (cod_produto);

create table tb_venda(
cod_venda integer identity(1,1) not null,
data_venda smalldatetime,
total_venda float not null,
fk_cliente integer not null,
fk_usuario integer not null
);
alter table tb_venda add constraint pk_venda primary key (cod_venda);
alter table tb_venda add constraint fk_venda_cliente foreign key (fk_cliente) references tb_cliente(matricula);
alter table tb_venda add constraint fk_venda_usuario foreign key (fk_usuario) references tb_usuario(cod_usuario);

create table tb_item_venda(
cod_item_venda integer identity(1,1) not null,
quantidade integer not null,
preco_item float not null,
fk_cod_venda integer not null,
fk_cod_produto integer not null
);
alter table tb_item_venda add constraint pk_item_venda primary key (cod_item_venda);
alter table tb_item_venda add constraint fk_item_venda_venda foreign key (fk_cod_venda) references tb_venda(cod_venda);
alter table tb_item_venda add constraint fk_item_venda_produto foreign key (fk_cod_produto) references tb_produto(cod_produto);

insert into tb_funcionario(cpf, nome) values( '123', 'admin');
insert into tb_usuario(user_login, senha, tipo_acesso, ativo) values ('123','123',1 ,1 );