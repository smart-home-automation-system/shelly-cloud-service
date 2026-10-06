# shelly-cloud-service

[![CI](https://github.com/smart-home-automation-system/shelly-cloud-service/actions/workflows/CI.yml/badge.svg)](https://github.com/smart-home-automation-system/shelly-cloud-service/actions/workflows/CI.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=smart-home-automation-system_shelly-cloud-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=smart-home-automation-system_shelly-cloud-service)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=smart-home-automation-system_shelly-cloud-service&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=smart-home-automation-system_shelly-cloud-service)

![GitHub Release Date - Published_At](https://img.shields.io/github/release-date/smart-home-automation-system/shelly-cloud-service?style=plastic)
![GitHub Release](https://img.shields.io/github/v/release/smart-home-automation-system/shelly-cloud-service?style=plastic)

---

![GitHub top language](https://img.shields.io/github/languages/top/smart-home-automation-system/shelly-cloud-service?style=plastic)
![Java](https://img.shields.io/badge/java-21-yellow?style=plastic)
![SpringBoot](https://img.shields.io/badge/SpringBoot-4.1.1-blue?style=plastic)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=smart-home-automation-system_shelly-cloud-service&metric=coverage)](https://sonarcloud.io/summary/new_code?id=smart-home-automation-system_shelly-cloud-service)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=smart-home-automation-system_shelly-cloud-service&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=smart-home-automation-system_shelly-cloud-service)

![GitHub issues](https://img.shields.io/github/issues/smart-home-automation-system/shelly-cloud-service?style=plastic)
![GitHub contributors](https://img.shields.io/github/contributors/smart-home-automation-system/shelly-cloud-service?style=plastic)
![GitHub pull requests](https://img.shields.io/github/issues-pr-raw/smart-home-automation-system/shelly-cloud-service?style=plastic)

![GitHub last commit](https://img.shields.io/github/last-commit/smart-home-automation-system/shelly-cloud-service?style=plastic)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/smart-home-automation-system/shelly-cloud-service?style=plastic)

---

# Description

Integration with the Shelly cloud API — the counterpart of `shelly-client`, which talks to the
Shelly devices directly over the LAN. It is meant to reach the same devices through the vendor
cloud, for the cases the local API cannot serve.

**Status: skeleton.** The service builds, starts and exposes its Actuator, but implements no
functionality yet — there are no endpoints and no Shelly cloud calls. It runs on the target
toolchain (Java 21 / Spring Boot 4.1.1) and on the org's observability scheme, so the first
feature lands on a finished foundation.

## Run locally

```bash
mvn verify                                  # build and tests
mvn spring-boot:run -Dspring-boot.run.profiles=home,local
```

| | Application | Actuator |
|---|---|---|
| local (`local` profile) | 6008 | 8008 |
| cluster (`home` profile) | 6200 | 8200 |

The Actuator exposes `health`, `info` and `prometheus`; the cluster logs are JSON (logstash),
locally they stay plain text.

## API

Base path `/home/shelly` (`spring.webflux.base-path`). No endpoints yet — this section grows
with the first feature.
