# 7.1 ORM Overview

## What is ORM?
Object-Relational Mapping (ORM) maps Java objects/classes to relational database tables. Instead of writing SQL for every CRUD operation, an ORM framework manages much of the mapping and persistence work.

### Core concepts
- Entity: Java class representing persistent data.
- Table mapping: class ↔ table.
- Field mapping: attribute ↔ column.
- Primary key: `@Id`.
- Relationships: one-to-one, one-to-many, many-to-one, many-to-many.
- Persistence context: tracks managed entity objects.
- CRUD: create, read, update, delete.
- JPQL/HQL: object-oriented query languages.
- Transactions: make database changes atomic and consistent.
- Lazy vs eager loading.
- First-level and optional second-level caching.

## JPA vs Hibernate
JPA/Jakarta Persistence is a specification/API. Hibernate ORM is an implementation of that persistence model and also exposes native APIs.

## Real-life use
ORM is common in e-commerce, banking, hospital, university, inventory and booking systems where Java domain objects need persistent relational storage.

## Advantages
1. Less repetitive JDBC/SQL code.
2. Object-oriented domain modeling.
3. Relationship mapping.
4. Transaction support.
5. Portable persistence logic.

## Limitations
1. Poorly designed queries can cause performance problems.
2. N+1 query problems can occur.
3. Complex reporting may still need native SQL.
4. Developers must understand SQL and database indexing.

## Study checklist
- [ ] ORM definition and purpose
- [ ] JPA vs Hibernate
- [ ] Entity and primary key
- [ ] Relationships
- [ ] Persistence context
- [ ] Transactions
- [ ] Lazy/eager loading
- [ ] N+1 problem
- [ ] CRUD and queries
