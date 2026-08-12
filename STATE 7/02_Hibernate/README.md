# 7.2 Hibernate

Hibernate ORM is a Java ORM framework. It supports object-relational mapping, persistence contexts, transactions, fetching, caching and HQL.

## Important terms
- `SessionFactory`: heavyweight, application-level factory.
- `Session`: unit of work with Hibernate's native API.
- `Transaction`: atomic database operation boundary.
- Entity: persistent Java class.
- HQL: Hibernate Query Language.
- Mapping annotations: commonly `@Entity`, `@Id`, `@GeneratedValue`, `@OneToMany`, etc.

## Entity lifecycle
Transient → Persistent/Managed → Detached → Removed.

## Practical project
The accompanying Maven project demonstrates a small inventory domain:
- Product entity
- Hibernate configuration
- CRUD operations
- Transaction boundary
- HQL query

## Production notes
Prefer Jakarta Persistence APIs where possible for portability. Use connection pooling, indexes, appropriate fetch strategies, batching, transaction management and query monitoring.

Current Hibernate documentation has version-specific guides; check the official docs before choosing a version for a new project.
