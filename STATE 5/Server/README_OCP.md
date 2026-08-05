# Open/Closed Principle (OCP)

Software entities should be open for extension but closed for modification.

## Example: OpenClosedService.java

The `PaymentService` accepts any `PaymentProcessor` implementation. New payment methods can be added without modifying existing code.

### Key Points
- New processors implement the `PaymentProcessor` interface
- `PaymentService` remains unchanged when adding processors
- Promotes polymorphic behavior
