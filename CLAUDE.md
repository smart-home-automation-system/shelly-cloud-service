# shelly-cloud-service

Integration with the Shelly cloud API — the counterpart of `shelly-client`, which talks to the
Shelly devices directly over the LAN. **Today it is a skeleton**: the application class, the
configuration and one context test. There are no endpoints, no clients and no calls to the
Shelly cloud.

Part of the smart-home-automation-system organization — org-wide conventions, the
repository map and working rules come from the workspace-level context
(`organization-repository/claude/organization.md`). The user writes the code in this
repository themselves; Claude's default role here is analysis, code review and security
review.

## Role in the system

- Calls: nothing yet. The Shelly cloud is outside the cluster, so the first client follows the
  org notes on calling an external system (every failure mapped, no harmless-looking defaults
  for mandatory connection settings, a wire model that cannot be swapped with the domain one).
- Is called by: nobody. `api-gateway-service` has **no route** for `/home/shelly`, so the
  gateway answers 404 there. The first endpoint needs `internal.service.shelly` and an entry
  in the gateway's `RoutesConfig` (predicate written without `/home`) — a gateway change and
  release of its own.
- No database and no RabbitMQ. The `cholewa-commons` R2DBC auto-configuration stays inactive —
  there is no R2DBC on the classpath and no `database.host`.
- Libraries on the classpath, none of them used by code yet: `cholewa-commons` (error
  handling), `smart-home-sdk`, `shelly-client`. MapStruct and its processor are wired in the
  pom for the first mapper.

## What the first feature has to deal with

- **`shelly.cloud.token` is a placeholder** (`dummy-token` in `application.yaml`), and nothing
  reads the `shelly.cloud.*` group. The repository is public: the real token comes from a
  Kubernetes secret through the manifest, never from this file. Bind the group with a
  `@ConfigurationProperties` class that validates it, and drop the placeholder then, so a pod
  without the secret fails at startup instead of calling the cloud with `dummy-token`.
- Build every `WebClient` from the injected `WebClient.Builder` — an own builder bean is not
  instrumented, and the outgoing call drops out of the trace.
- Bean Validation is already on the classpath — `cholewa-commons` brings
  `spring-boot-starter-validation` — and the library's English validation messages are on, with
  nothing to act on: there is no constraint anywhere. The first validated input brings the
  consumer-side locale test described in `organization.md` (a `@SpringBootTest` on a Polish
  JVM expecting English, plus a control with the property switched off).
- A scheduled job that calls the cloud must not run in a test context: `@EnableScheduling` on
  a configuration class under `@Profile("!test")`, as in `boiler-service`.

## Tests

- Surefire activates the `test` profile for every class (`systemPropertyVariables` in the
  pom), and the `test` document of `application.yaml` switches the console back to plain text
  and logbook to the `http` style. Without it a `@SpringBootTest` context installs the
  logstash encoder for every test that follows in the same JVM.
- Surefire sets the profile only under Maven, so **every context-starting test class also
  carries `@ActiveProfiles("test")`** — started from an IDE without it, the class comes up
  with `home`.
- `ShellyCloudServiceApplicationTest` is the only test: the proof that the whole context
  starts with the libraries as they are. It is what a library or Boot bump has to pass.
- HTTP stubs: `com.squareup.okhttp3:mockwebserver3` (`mockwebserver3.*`,
  `MockResponse.Builder`, `close()`). Do not go back to the legacy `mockwebserver` artifact —
  it puts JUnit 4 on the classpath, where a JUnit 4 test compiles and never runs.
- Surefire includes only `**/*Test.java` and `**/*IT.java`; a class named `...Tests` is
  silently skipped.

## Build & run

- Build: `mvn verify` (JDK 21). The enforcer runs `dependencyConvergence`, so a new
  dependency with a conflicting transitive version fails the build — pin the version in
  `dependencyManagement`, do not exclude annotations javac needs.
- Run locally: `mvn spring-boot:run -Dspring-boot.run.profiles=home,local` — application on
  6008, Actuator on 8008, plain-text logs. Nothing is called on start, so a local run is safe.
- In the cluster: 6200 / 8200, probes on `/actuator/health/{readiness,liveness}`, JSON logs,
  tracing without an exporter (see the org context).
- Release: `gh release create <X.Y.Z>` triggers `release.yml`, which pushes
  `magikabdul/shelly-cloud-service:<X.Y.Z>` to Docker Hub; the manifest lives in
  `deployment-tools` (`workshop/shelly-cloud-service.yaml`). Tags have no `v` prefix. Release
  flow: the `release` skill.
- The repository started as a copy of `boiler-service`. The image name, the Discord titles
  and the README were corrected in HAS-129; when something here still says "boiler", it is a
  leftover, not a convention.
