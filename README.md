# URL Shortener

A simple URL shortening REST API built with **Java and Spring Boot**.

The application accepts a POST request containing an original URL in a JSON request body and generates a unique, eight-character shortened code.

Each shortened URL is stored in memory as an object containing:
- A unique numeric ID
- A randomly generated short code
- The original destination URL

Users can access their original URL by visiting:

`http://localhost:8080/{code}`

The server looks up the corresponding URL and returns an HTTP redirect to the original destination.

## Features

- **URL shortening:** Generates randomized eight-character short codes.
- **URL validation:** Validates URI structure, schemes, hosts, and ports.
- **Collision handling:** Prevents duplicate short codes from overwriting existing mappings.
- **Concurrency control:** Uses synchronization and a concurrent hash map to manage simultaneous requests.
- **Automated testing:** Includes JUnit tests covering validation, storage, code generation, and concurrent URL creation.

## Technologies Used

- Java
- Spring Boot
- Maven
- JUnit
- ConcurrentHashMap

## Current Limitations

- URLs are stored in memory and are lost when the application restarts.
- The application currently runs locally.
- The project is intended as a backend engineering learning exercise rather than a production-ready URL shortening service.

## Requirements

- JDK 17 or later
- An internet connection for the Maven Wrapper's initial setup and dependency downloads

No separate Maven installation is required.

## How to Run

Clone the repository and navigate to the project's root directory.

**Windows (PowerShell):**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**

```bash
./mvnw spring-boot:run
```

Once the application starts, it will be accessible at `http://localhost:8080`.

**Note:** The application has only been tested on Windows. Compatibility with Linux and macOS has not yet been verified.

## API Endpoints

The application exposes two endpoints:

### 1. Shorten a URL

**`POST /shorten`**

Accepts a JSON request body containing the original URL and generates a unique shortened code.

**Example request:**

```http
POST http://localhost:8080/shorten
Content-Type: application/json
```

```json
{
  "url": "https://example.com"
}
```

**Example successful response (`200 OK`):**

```text
Successfully created URL with code: aB12xY34
```

### 2. Redirect to the Original URL

**`GET /{code}`**

Accepts a previously generated short code and redirects the client to the corresponding original URL.

**Example request:**

```http
GET http://localhost:8080/aB12xY34
```

**Successful response (`302 Found`):**

The server returns an HTTP redirect with a `Location` header pointing to the original URL.

If the code doesn't exist, the server returns `Not a valid URL`.
