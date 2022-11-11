# Feature Flag Service [![Build Status](https://app.travis-ci.com/rj90/feature-flag-service.svg?token=tgzKbGYs8ivFig58CkTy&branch=main)](https://app.travis-ci.com/rj90/feature-flag-service) [![Coverage Status](https://coveralls.io/repos/github/rj90/feature-flag-service/badge.svg?branch=main)](https://coveralls.io/github/rj90/feature-flag-service?branch=main&service=github)

Feature flag service for creating feature permissions and assigning them to users

## Prerequisites
* Java 17 - On standalone build

## Building

To build app use following command
```bash
./gradlew build
```
If you want to make full rebuild you should clean whole project using:
```bash
./gradlew clean
```

## Running

Use following command to run it locally
```bash
./gradlew bootRun
```

## H2 Access

H2 database is available in:
```
localhost:8080/h2-console
```
Url is `jdbc:h2:mem:featureflagdb` and user `SA`. No password

## Api access

Api is available on this link:
```
http://localhost:8080/swagger-ui/index.html
```
Api is secured and access is available after login.
There are two types of users by default:
* ADMIN (one user with credentials admin:admin)
* USER (three users: user1, user2, user2 with password same as login)

Admin has full access to all apis. User has only access to one api which allows him to check his feature-flags

## Testing

All tests are run during build. Also they can be run using this command
```bash
./gradlew test
```