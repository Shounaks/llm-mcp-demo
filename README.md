# LLM-MCP-Demo

A demonstration of integrating Large Language Models (LLMs) with the Model Context Protocol (MCP) to enhance AI agent capabilities.

---

## 🚀 Overview

This project showcases how LLMs can be augmented with external tools and data sources using the Model Context Protocol (MCP). By leveraging MCP, LLMs can access and utilize various services, enabling more dynamic and context-aware interactions. The system is completely reactive, supporting asynchronous and non-blocking operations.

---

## 🧩 Features

* **MCP Integration**: Seamlessly connects LLMs with external tools and data sources.
* **Reactive Architecture**: Fully reactive design using Kotlin coroutines and Spring WebFlux.
* **Caching with Caffeine**: Efficient caching layer to improve performance and reduce redundant calls.
* **Rate Limiter**: Protects external services from excessive calls.
* **Modular Architecture**: Easily extendable to support additional tools and services.
* **Real-world Use Cases**: Demonstrates practical applications of LLMs in real-world scenarios.

---

## 🛠️ Technologies Used

* **Kotlin**: The primary programming language for the project.
* **MCP**: Protocol facilitating communication between LLMs and external tools.
* **Spring WebFlux**: Reactive web framework.
* **Caffeine**: Caching library.
* **Resilience4j or similar**: Rate limiting.
* **Gradle**: Build automation tool used for project management.

---

## 📦 Installation

### Prerequisites

* [JDK 17 or higher](https://adoptopenjdk.net/)
* [Gradle](https://gradle.org/install/)

### Steps

1. Clone the repository:

   ```bash
   git clone https://github.com/Shounaks/llm-mcp-demo.git
   cd llm-mcp-demo
   ```

2. Build the project:

   ```bash
   ./gradlew build
   ```

3. Run the application:

   ```bash
   ./gradlew run
   ```

---

## 🧪 Usage

Upon running the application, it will initiate an MCP server that listens for incoming requests. These requests can be processed by the integrated LLMs, which can then interact with external tools and data sources as defined in the MCP configuration. The caching and rate limiting mechanisms ensure high performance and stability in reactive environments.

---

## 🧠 Contributing

We welcome contributions to enhance the functionality and capabilities of this project. To contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/YourFeature`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Create a new Pull Request.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 📞 Contact

For any inquiries or feedback, please reach out to [Shounaks](https://github.com/Shounaks).
