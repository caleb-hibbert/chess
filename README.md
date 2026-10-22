# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
Phase 2 sequence diagram:
https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoARiqfKR3y-P8gKoQ2oEVEBzzgeWKlaSC6k7AWtGYHpUAVG+XYIEJ4oYoJwkEkSYCkrZFTDvytSMiybLKVyN40vuFTLmKEpujKcplu8SqYCqwYagAchAsXarq+p6oY2DeFQSAcCg66BTAVo2tuKAADylN59I9n26FQNVtm1G6qVgCKvicFGMZxiJyDmM11TPOmACMBE5qoebzNBRYlvUJVoBAzAAGZdbsdFNrZ1W1aOUVbp55S7b5TJNPofw7NOhlBbtYXChF65umdWw7LK8qBRVO23j5T3nRpCWqhq7TxDleUFUVMCmQCRgFWgADkzDHGAIBZSDxrgD4JqQ39r1rbAepyLFnKNUNna1AAkmgIDQCi4DPRdAI9SgsYKUUA0YKTNS1GNE38tN5lzdA9TZZDYOFeuUPrps+gwOK1PFig4ANvRFVed99KHnIKDPvE56Xtet3lA+a4BvrW5WZzSLOeKGSqABJOlFZtQGXFxGqbBl5UfWJFaVcomc88uH4aMhHXTRZFjBRXvIT7Fkq543h+P4XgoOgMRxIkKdp85vhYKJgqgfUDTSBG-ERu0EbdD0cmqApwwUdASAAF55AU9SVVHiHoOUjW6Y8cK1A3UDN63aDt53SE91ZNnOnZQm5058+nq5ajuarR3q6Ofk63r8Fd2gc4hSOd2iuKT5m-Ib3Gp7+8A0lMCpabL7aDASMo2oMAi1kNBoE-lG39tGqm9aj1X7KBS2LpmTTEvI3JuRUmYs3jP7UohceYh0mvzWaxYhYKmgXqWBRVlZbVnl9I+PkdavlnmrMh9JCrcGPBfYAu9-5IUPryY+RthTSBQPQwwFCX4xQnugSy-dKAQK7DnZe-4EBYHAY7URdQyA+H3GzEoYAA51CDlmTaDEE7MX8Cidc-hsDig1PxNEMAADiSoND53ElzRoljy5V3sEqeuMDh4tz6uPG+k9e7lCdkPEe3iYAd18d3ERsJKAzzJn5axOYMSWKZPEtQK8STr2OtvS8zDo5sIXKoE+D0-7RXlEIwoiV1QPzSvwy+b8QYCi-kgH+f9o6fSATQ0coCHYtSgUEuBHAEF9VUYNFBw06hoOzHzfMWD5q4L6YQnRbTjo1OAKYQ6mTkk2IxHk0KnDT4ShNAgdGBAaAwFcTmJZwDzE5ESUqa8FtRlk2uWAFJqhbb2zkU7cY5y1AFgaN8pU5NpAFlGuEYIgQQSbHiLqFAbpOTmQjskUAao4WQXdssH5yU0W+06H7dm6jHlcy0SHH5qg-kArmECkFYKIXLChTC1FRkYLLCRSAFFREZrGQxUqLFTLLi4sbAxJiScOAAHY3BOBQE4GIEZghwC4gANngBOQwKSYCmDsb6MZRdWgdBcW4vB8RYGjx8XvSeWZuVzF5Zy-YMArg6QCQoweHjgkKVNSw7uFqxiYuxba6ECiYn5RdJrdEKSMRwBVSktJa91nAKyWeMpOyOHhTPsUgRpTwnlMBlUtNtS5DvwaWjb+acdatMAcdLpciemGoIQM6MzMhnINQU4ca6Cpk2sFqWHwNbPELMFZcjpK5GHrxau-NlKTrUADUTQFTgPEPAoB0RQA0BUoGaNcoQHyuLM5FYjTSwFEcWM8poAwGphgE4aAIbiioDO9cKMF3gEoBoKts8KZoBvcgDgc6H1LtUIM1mTbtXpmDiMZYGDpmFmwaWPQ64USElXksRZGTgEhpQGGn1cwk38hPo+KxdzzYKPEbUCNR40NKneTIh2XzvWAuBfUUF4K8VqI0bUYlFKUBUvozSpDwqAiWB4fZTY6ckAJDAPxvsEAhMACkIDijw3MGIrK1TDLKFqiSpZmjMhkj0H57j8GeJNaEsp5QCLYAQMAfjUA4AQHslABF3wfmcd9v4wJLqvFuqM5mkzIczMWcoNZ2z9mQSObo856e5QWoACtZNoDDTJ8UUaUDwfSbGwd8bdaJuCuw7DeyikrKvmUu+lTH75bqR-RpzTS0AJIe07LdVexgLGUR3pbn4H1sQf1ZjhKRott5rmCDYxO0LR7c3PtKty3AJWSO19Y61QpKBdOz936oCLqfUVtdoNN3g3XD8mAQLtQy0PWJmAJ6z3yHFFe99t7T3zpW4+5d3TX2Uw-bO27q3l3-qQfiljwGvXgY7VB+oMGYBwbcoh-tqW6tb02XMDEIWsPqEKam3bMUQvrfk4YDgEA1Dw2YGVHIA6ocgIa49smz3b0pM+51kZzaQNjH+wLQHsoQwg6S2DxZE3B0pMoZ2ahRPZvkxWgF6ACgb2nF0NwOHtGFCcGF1ABHBTcuppFm8GAGIkArWNGgSwxJjTqAgEQLWUtMqf3XWLCG1NbMwDxqbzbW6Ia7aBejmXy0QawFV9kSsZp5S+csOKRwMsRag9Xhbmz0BCf5NqG8HnNQiOBm92GJCPVsBMgA997r4yMx-fbYz2ZJpvef3DDHDnNWK0k8h5HvwWhQ1Kil5S6QMuOCMu1rLsP8usv5KRxKL+FZUe0a1+uEM64VdakgLbmANhFYa9QHetvkSvxEfi7F8j0jZFNfkVExRyiRwqYJYXNjPHE4BC8BZ4TomT-ykQMGWAwBsBmcIKPdVmqOwOOLqXculdejGBc06kuZcK5f4mDhajrcB4DhqgFQDRoeRUIbyDogAQE8h6AGDbId67LLgZAzCbqGCHKxSqBrCkprCPDXxmroAaAPItRX54AUZr7qbVROxKIqKAbqasZ4TaL9pAA
