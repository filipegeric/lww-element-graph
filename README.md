# LWW Element Graph

**Last-write-wins element graph**

A combination of LWW-Element-Set and a directed graph

## Run tests

```shell
./gradlew clean test
```

## Implementation

Graph consist of vertices and edges. Both are stored in an LWW-Element-Set.
Edges are represented as pairs where direction is `pair.first` -> `pair.second`.

## Merging graphs

Merging graphs consists of merging vertices set and merging edges set.
Merging graphs is:
1. Commutative `(x * y) = (y * x)`
2. Associative `(x * y) * z = x * (y * z)`
3. Idempotent `x * x = x`

All these properties are tested in `com.goodnotes.LWWElementGraphTests`