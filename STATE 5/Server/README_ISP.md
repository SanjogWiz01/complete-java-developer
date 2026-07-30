# Interface Segregation Principle (ISP)

Clients should not be forced to depend on interfaces they do not use.

## Example: InterfaceSegregationService.java

`Workable`, `Eatable`, and `Restable` are separate interfaces. `HumanWorker` implements all three. `RobotWorker` implements only `Workable`, avoiding unnecessary dependencies.

### Key Points
- Fine-grained interfaces prevent fat interfaces
- Clients implement only what they need
- Reduces coupling and improves cohesion
