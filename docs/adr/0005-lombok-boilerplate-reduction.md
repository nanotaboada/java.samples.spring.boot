# ADR-0005: Use Lombok to Reduce Boilerplate

Date: 2026-06-07

## Status

Accepted

## Context

Java entity and DTO classes require getters, setters, constructors, `equals`/`hashCode`, and `toString` — several dozen lines of mechanical code per class. Options considered: Java Records (immutable, no setters, JPA requires a no-arg constructor), manual boilerplate (verbose, noisy diffs), MapStruct-only (covers mapping but not field access), and Lombok (annotation-driven code generation at compile time, widely adopted in enterprise Spring projects).

## Decision

We will use Lombok with `@Data` on DTOs (getters, setters, equals, hashCode, toString), `@Builder` where builder construction is needed, `@RequiredArgsConstructor` for constructor injection on Spring beans, and `@AllArgsConstructor` on entity/DTO classes that need a full-argument constructor.

## Consequences

- `@RequiredArgsConstructor` on `@Service` and `@RestController` classes generates a constructor for all `final` fields, enforcing constructor injection without writing the constructor manually.
- Entity and DTO classes remain short and readable; diffs show domain changes rather than boilerplate churn.
- Lombok requires IDE annotation-processing support (IntelliJ IDEA, Eclipse). Without it, the generated methods are invisible to the IDE and appear as compilation errors.
- Java Records are a compelling alternative for immutable DTOs but cannot be JPA entities without workarounds. Lombok is the pragmatic choice until JPA tooling for records matures.
- Lombok uses compile-time bytecode manipulation; updates to JDK internals (as seen in JDK 16+ access controls) have occasionally broken Lombok and required a new release. This dependency on Lombok's release cadence is an accepted trade-off.
