# Liskov Substitution Principle (LSP)

Subtypes must be substitutable for their base types without altering the correctness of the program.

## Example: LiskovSubstitutionService.java

`FlyingBird` and `NonFlyingBird` extend `Bird` appropriately. `Sparrow` (flies) and `Penguin` (does not fly) follow correct hierarchies, avoiding broken substitutions.

### Key Points
- Base class `Bird` only contains behavior common to all birds
- `FlyingBird` adds flight capabilities
- Clients use the correct abstraction level
