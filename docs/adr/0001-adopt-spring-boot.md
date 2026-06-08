# ADR-0001: Adopt Spring Boot as REST API Framework

Date: 2026-06-07

## Status

Accepted

## Context

The project needs a production-grade Java framework for building a CRUD REST API. The Java ecosystem offers several mature options: Quarkus (GraalVM-native-first, reactive), Micronaut (compile-time DI, fast startup), Jakarta EE + Jersey (standard but verbose), and plain Spring MVC without Boot (full control, high configuration cost). The project is a proof of concept and learning reference — part of a cross-language comparison series — so ecosystem familiarity and discoverability matter as much as raw performance.

## Decision

We will use Spring Boot 4.0.0 targeting JDK 25 LTS as the REST API framework.

## Consequences

- Spring Boot's auto-configuration dramatically reduces bootstrap code; a working API is reachable with minimal configuration.
- The embedded Tomcat server removes external server setup, simplifying local development and containerisation.
- Spring Boot has the largest community, the most StackOverflow answers, and the widest industry adoption of any Java framework — learners and contributors encounter familiar patterns.
- The cross-language comparison series favours the dominant framework in each ecosystem; Spring Boot is the canonical Java choice.
- Spring Boot's opinionated defaults can be overridden but require explicit effort; contributors unfamiliar with autoconfiguration may be surprised by what is wired automatically.
- Spring Boot 4.0 requires JDK 17+ and drops several legacy APIs, which constrains the minimum runtime version.
