# ADR 0002: Valores monetários serão encapsulados em Value Object

- Status: Adotada

## Contexto

Lidar com dados monetários requer alta precisão e centralização de lógicas que a manipulam como soma, subtração, etc.

## Decisão

Todos os valores monetários da aplicação serão encapsuladas por um value object chamado `Money`, ele irá definir as lógicas de cálculo necessárias.
Os valores monetários serão persistidos no banco de dados em `centavos`.
Os valores armazenados em `Money` não serão negativos e terão apenas 2 casas decimais.

## Consequências

- Todo valor monetário recuperado do banco de dados deve passar por transformação, sendo dividido por 100.

## Referencias

- `src/main/java/api/financas/domain/valueobject/Money.java`
