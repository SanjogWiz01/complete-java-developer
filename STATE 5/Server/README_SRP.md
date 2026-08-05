# Single Responsibility Principle (SRP)

A class should have one and only one reason to change.

## Example: SingleResponsibilityService.java

The `UserRegistrationService` handles registration logic while validation, persistence, and notification are delegated to separate responsibilities within the class. Each method has a single purpose.

### Key Points
- Each method does one thing
- Changes to validation logic don't affect persistence logic
- Easier to test and maintain
