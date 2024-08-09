# Webhook Sender Application

The Webhook Sender Application is designed to send webhooks with a robust retry mechanism using an exponential back-off strategy. This ensures reliable delivery of webhooks even in the face of temporary network issues or endpoint unavailability.

## Setup and Running

Follow these steps to set up and run the application:

1. **Ensure Prerequisites Are Installed**:
   - [Kotlin](https://kotlinlang.org/docs/home.html)
   - [Java Development Kit (JDK)](https://openjdk.java.net/)
   - [Gradle](https://gradle.org/install/)
   - see file [SETUP-README.md](https://github.com/Mole93/Webhook-Sender/blob/main/SETUP-README.md)

2. **Build the Project**:
   - Open your terminal and navigate to the project directory.
   - Run the following command to compile the Kotlin files and create a JAR file:
     ```bash
     ./gradlew clean build jar
     ```

3. **Prepare for Execution**:
   - Ensure that the `webhooks.txt` file is located in the `build/libs` directory. This file should contain the list of webhooks to be sent.

4. **Run the Application**:
   - Execute the following command to start the application:
     ```bash
     java -jar build/libs/WebhookSender-1.0-SNAPSHOT.jar
     ```

## Running Tests

You can verify the functionality of the application by running tests:

1. **Execute Tests**:
   - Run the following command to execute the test suite:
     ```bash
     ./gradlew test
     ```

2. **View Test Reports**:
   - After the tests complete, navigate to `build/reports/tests/test`.
   - Open `index.html` in your browser to view the detailed test report.

## Design Decisions and Considerations

### 1. Exponential Back-off Strategy
- The strategy is implemented using the Strategy pattern, with `ExponentialBackoffStrategy` as the core implementation.
- `ExponentialBackoffStrategy` contains the logic for exponential retries and is configurable through its constructor, allowing easy adjustments to retry parameters.
- The `WebhookSender` class leverages the `BackoffStrategy` interface for retry logic and the `NetworkClient` interface for HTTP requests.
- A `ThreadUtils` interface is used to simulate delays in tests, speeding up test execution.

### 2. Endpoint Failure Handling
- The application tracks failures for each endpoint within the `ExponentialBackoffStrategy` class.
- Sending attempts to an endpoint are halted after five consecutive failures to prevent further unnecessary retries.

### 3. Queue Implementation
- Utilizes a `ConcurrentLinkedQueue` to ensure thread-safe operations.
- Simple in-memory queue without persistence (trade-off for simplicity).

### 4. Security Considerations
- Uses HTTPS for secure communication (assuming URLs in the input file use HTTPS).
- Does not implement authentication for simplicity, but in a real-world scenario, you'd want to add authentication mechanisms.

### 5. Error Handling
- Catches and logs exceptions during webhook sending.
- Continues processing the queue even if individual webhooks fail.

### 6. Trade-offs
- **Simplicity over Scalability**: This implementation works well for moderate loads but may not scale to very high volumes.
- **Lack of Persistence**: Webhooks are lost if the application crashes (could be improved with a persistent queue).
- **Synchronous Processing**: Each webhook is processed sequentially, which may not be optimal for high-throughput scenarios.

---