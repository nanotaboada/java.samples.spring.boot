# ADR-0003: Implement In-Memory Caching with Spring Cache

Date: 2026-06-07

## Status

Accepted

## Context

GET endpoints benefit from caching to avoid repeated database reads for stable data. Options considered: Redis (external server, TTL support, distributed), Caffeine (local, configurable TTL and eviction, no external process), Hazelcast (distributed, clustering), and Spring's built-in simple `ConcurrentHashMap`-backed provider (zero configuration, no expiry). The project is a PoC with no high-availability requirement, and adding an external cache service increases setup friction without a meaningful payoff.

## Decision

We will use Spring's `@Cacheable` abstraction with the default simple in-memory provider (backed by `ConcurrentHashMap`). No TTL or eviction policy is configured.

## Consequences

- Zero external dependencies: the cache works out of the box with no additional configuration or infrastructure.
- The `@Cacheable`, `@CachePut`, and `@CacheEvict` annotations are placed on service methods, demonstrating the Spring caching pattern in a realistic but minimal way.
- Switching to Caffeine or Redis in the future requires only a dependency addition and property changes; the annotation-based API remains the same.
- With no expiry, cached data reflects the state at the time of the first load. Updates via PUT trigger `@CacheEvict`, but a restart is required to clear the cache fully in production.
- In-memory cache state is lost on restart and is not shared across instances — not suitable for horizontally scaled deployments.
