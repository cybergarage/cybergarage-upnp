# Security remediation — 2026-10-22

The [2026-10-05 audit](2026-10-05-audit.md) recorded nine validated findings against revision `f4a1ada0188cd5f8afc13391c8346b3ef5eaed50`. This document describes the subsequent remediation; the archived audit retains its original findings, historical paths and line numbers. Its canonical JSON artifacts remain in the local Codex Security workbench and are not included in this repository copy.

## Changes and regression coverage

| Finding | Shared enforcement boundary and fix | Regression coverage |
| --- | --- | --- |
| External XML entities | `JaxpParser` rejects DOCTYPE and disables external entity/DTD/schema access and XInclude. Unsupported security settings fail parsing. | Local-file, external URL, parameter and internal entity declarations are rejected; ordinary XML and escaped text still parse. |
| Truncated chunked HTTP CPU loop | `HTTPPacket` reads chunk bodies and CRLF exactly and rejects premature EOF or zero progress. | Every truncated prefix of a multi-chunk body fails within a bounded test; valid chunks, extensions, trailers and `100 Continue` succeed. |
| Unbounded HTTP buffering | Bound lines, total headers/count, buffered content and wire bytes before application dispatch. Reject duplicate/ambiguous framing and oversized aggregate chunks. | Oversized lines/headers/bodies, invalid lengths and chunk sizes fail; a body exactly at the content limit and bounded close-delimited responses succeed. |
| Unlimited HTTP workers | Admit at most 32 accepted connections per HTTP server. Reject excess connections and release sockets/capacity in `finally`, including listener failures. Close/stop also closes accepted sockets. | Stalled-client saturation rejects an excess client; cleanup, idle timeout and reopening the server are exercised. |
| GENA callback SSRF | Require an HTTP callback whose resolved address matches the incoming subscriber's actual peer address. Pin delivery to that numeric address, avoiding subsequent DNS resolution. Preserve query strings and default port 80. | Unrelated and remote-to-loopback targets, unsupported schemes, userinfo, fragments, control characters and invalid ports fail. Same-peer IPv4/IPv6 and localhost callers remain supported. |
| Missing outbound deadlines | Route URL description retrieval through the bounded HTTP client, with finite redirects and a shared remaining time budget. A socket-closing timer covers connect, TLS, response headers/body and writes. HTTPS retains certificate/hostname verification. | Silent and trickled-header peers terminate; malformed keepalive responses fail and close. HTTP redirects preserve query strings; trusted local HTTPS succeeds and a hostname mismatch fails. |
| Malformed SSDP HOST | Validate numeric IPv4/bracketed IPv6 before indexing or resolution. IPv4 is constructed from octets; hostname-shaped hexadecimal strings cannot trigger DNS. Malformed values return null and are rejected by the existing multicast filter. | Empty/colon/bracket/port errors and hostname-shaped inputs fail; normal IPv4/IPv6 multicast addresses succeed. |
| Unbounded SSDP MX delay | Reject nonpositive MX, cap positive MX at five seconds before multiplication, and run search listeners outside the receiver in one worker with a queue of at most 32 tasks per socket. Drop excess queued UDP work. | Maximum integer MX returns within the bounded delay; invalid MX fails. A blocked listener does not stop the receiver consuming new packets; stopping cancels queued work. |
| Recursive media container cycles | Forward the intended recursion flag and bound visited container IDs, depth, node count and traversal time. Reject cycles/budget violations explicitly. | Self-cycles, two-node cycles and deep unique chains fail; finite trees and all combinations of recursion/metadata flags retain their intended behavior. |

## Compatibility and limits

- Buffered HTTP/XML content: 8 MiB; HTTP line: 8 KiB; headers/trailers: at most 100 and 64 KiB aggregate. HTTP wire parsing also has a finite aggregate byte cap. These limits apply to buffered control/description input, not outgoing media streams served through `HTTPSocket`.
- HTTP message parsing and default outbound operations have a 30-second budget. `HTTPRequest.setResponseTimeout(int)` sets a positive outbound transaction timeout in milliseconds. Existing accepted-socket inactivity timeout configuration remains available; the transaction/message budget prevents indefinitely refreshing that timeout.
- Only HTTP and HTTPS description URLs are accepted. At most five same-protocol redirects are followed. Cross-protocol redirects fail explicitly.
- Media traversal limits: 32 levels, 1,024 visited container IDs, 10,000 returned nodes and a 30-second traversal budget, in addition to individual request deadlines.
- Third-party callback destinations are now rejected. Callers using proxies or a callback host different from their subscription peer must change that deployment arrangement. Loopback callbacks remain valid for loopback subscribers.
- Invalid or oversized HTTP messages now fail closed instead of producing a partially parsed successful response. A server may close a rejected connection rather than return an application response.

## Verification

Outcome: **fixed** for the nine validated source paths. The malicious inputs listed above no longer reproduce the reported behavior in the regression suite; normal XML, HTTP framing, HTTP/HTTPS description retrieval, local subscriptions, finite media browsing and existing device discovery controls remain covered.

- Compile and owning-project checks: `mvn -B -Dmaven.compiler.release=11 -Dmaven.javadoc.skip=true -Dgpg.skip=true clean verify` — passed across all modules, using Maven 3.9.9 and JDK 17 with Java 11 API/bytecode targeting. JaCoCo 0.8.8 instrumentation and aggregate report generation succeeded.
- Security triggers and legitimate controls: 40 tests passed (38 core, 2 std), with zero failures, errors or skipped tests. This includes 26 security regression tests and 14 existing tests.
- Candidate review: one independent read-only bypass/regression review was completed. Its confirmed DNS validation, description-header deadline and generated Javadoc link issues were addressed before the final clean verification.
- Formatting: Google Java Format 1.24.0 with `--skip-sorting-imports`; `git diff --check` — passed.
- Documentation: `bash -n javadoc doxygen` passed; all nine moved files match their latest `origin/master` Git blob hashes; README, programming-guide and remediation relative links resolve, including the overview's rewritten `../...` links from `docs/javadoc/`.
- Full Javadoc/Doxygen rendering was not run locally because Pandoc and Doxygen are unavailable. Their configuration, shell syntax and migrated paths were checked; generated output remains ignored.

The checks use temporary Maven dependencies and a temporary JDK; no tool installation or credentials are committed. Network tests use local sockets, and the HTTPS test generates an ephemeral localhost certificate rather than retaining a private key in the repository.

## Documentation consolidation

All former `doc/` content, including the programming guide, PDF and images, now lives under `docs/`. Doxygen input/output and image paths, Javadoc output, generated-overview links, GitHub Pages publication and ignore rules use the unified location. Generated `docs/javadoc/` and `docs/doxygen/` content remains ignored.

## Remaining uncertainty

The three deferred audit leads remain outside the nine confirmed findings: remote SCPD local-file precedence needs an application-visible disclosure sink; deeply nested XML impact depends on parser/runtime behavior; and unvalidated GENA event delivery needs a security-sensitive application outcome. This patch does not claim to resolve those leads or replace deployment-specific authorization, firewalling or dependency-advisory review.
