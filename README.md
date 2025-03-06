# 02 Spring Core Notes

## 1. Types of Injection

### a. Constructor Injection (Best Practice)
- Use it when you have required dependencies.
- Generally recommended by the [Spring.io](https://spring.io) dev team as a first choice.

### b. Setter Injection
- Use this when you have optional dependencies.
- If a dependency is not provided, your app can provide reasonable default logic.

### c. Property Injection
- **Not recommended** by the Spring.io dev team because it makes unit testing harder.

---

## 2. @Autowired
- Injects a class implementation.
- Spring scans `@Component` classes.
- Checks if any class implements the interface.
- If multiple implementations exist, it needs further resolution (see `@Qualifier`).

---

## 3. @Qualifier
### a. Constructor Injection
- `@Qualifier("className")` is passed in the constructor.
- `@Qualifier` uses the bean ID (same as class name with a lowercase first letter).

### b. Setter Injection
- Works similarly to constructor injection but applied in the setter method.

**Note:** Bean IDs start with a lowercase letter.

---

## 4. @Primary
### How it Works
- Add `@Primary` annotation to the preferred class.
- No need to add `@Qualifier`.

### Notes
- `@Qualifier` has **higher priority** than `@Primary`.
- You **cannot** have more than one `@Primary` bean, or an error will occur.

### @Primary vs @Qualifier
- `@Primary` leaves it up to the implementation and may cause issues if multiple `@Primary` classes exist.
- `@Qualifier` is **more specific** and has **higher priority**.
- **Recommendation:** Use `@Qualifier` when possible.

---

## 5. Initialization
- By default, when the application starts, all beans are initialized (`@Component`, etc.).
- Spring creates an instance of each and makes them available.

**Debugging Tip:** Add `println` to the constructor to check if a bean is initialized.

---

## 6. Lazy Initialization
- Instead of creating all beans upfront, we can specify lazy initialization.
- A bean is initialized only if:
  - It is needed for dependency injection.
  - It is explicitly requested.

### How to Enable Lazy Initialization
- Add `@Lazy` to each class (**tedious for many classes**).
- **Global Solution:** Set the property in `application.properties`:
  ```properties
  spring.main.lazy-initialization=true
  ```
  - Now all beans are lazy-loaded, including controllers (loaded only when an endpoint is hit).

### Pros & Cons
**Advantages:**
- Only creates objects when needed.
- Faster startup time for large applications.

**Disadvantages:**
- Web components (`@RestController`) are not created until requested.
- Potentially delayed configuration issue detection.
- Need sufficient memory for all beans when they are eventually created.

**Note:**
- Disabled by default.
- Profile your application before enabling lazy initialization.
- Avoid premature optimization pitfalls.

---

## 7. Bean Scopes
### What is Bean Scope?
- Defines the **lifecycle** of a bean.
- Determines **how long** the bean lives.
- Controls **how many instances** are created.
- Manages **how the bean is shared**.

**Default Scope:** Singleton.

### Singleton Scope
- **One** instance per Spring container.
- Cached in memory.
- All injections reference the **same instance**.
- Explicitly define as:
  ```java
  @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
  ```

### Other Bean Scopes
| Scope      | Description |
|------------|------------|
| **singleton** | One shared instance (default). |
| **prototype** | A new bean instance for each request. |
| **request** | Scoped to an HTTP request (web apps only). |
| **session** | Scoped to an HTTP session (web apps only). |
| **application** | Scoped to a ServletContext (web apps only). |
| **websocket** | Scoped to a WebSocket connection (web apps only). |

### Prototype Scope
- Creates a **new instance** for each injection.
- Defined as:
  ```java
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  ```
- **Debugging Tip:** Check scope by adding a route mapping:
  ```java
  @GetMapping("/check")
  public String check() {
      return "Comparing Beans: Bean1 == Bean2, " + (Bean1 == Bean2);
  }
  ```

---

## 8. Bean Lifecycle
### Lifecycle Stages
1. **Container Started** → **Bean Instantiated** → **Dependencies Injected** → **Internal Spring Processing** → **Custom Init Method** → **Bean Ready** → **Container Shutdown** → **Custom Destroy Method** → **Stopped**

### Custom Hooks
- Custom logic during bean initialization:
  - **@PostConstruct**: Runs after initialization.
  - **@PreDestroy**: Runs before destruction.

### Prototype Beans & Destroy Lifecycle
- Spring **does not call** destroy methods for `prototype` scoped beans.
- **Why?** Spring does not manage the full lifecycle of `prototype` beans.
- **Solution:** Client code must clean up prototype-scoped objects manually.

---

## 9. Java Bean Configuration
### Steps
1. **Create a configuration class** and annotate with `@Configuration`.
2. **Define beans using `@Bean` methods**:
   ```java
   @Configuration
   public class SportConfig {
       @Bean("aquatic") // Custom bean ID
       public Coach swimCoach() {
           return new SwimCoach();
       }
   }
   ```
3. **Inject the bean using `@Qualifier` with the bean ID.**

### When to Use @Bean
- Making **third-party classes** available in Spring.
- When you **do not have access to source code** (e.g., AWS SDK).
- When a class **cannot be annotated** with `@Component`.

### Real-World Example: AWS S3 Client as a Spring Bean
- AWS SDK provides an S3 client but does not allow modification.
- We **cannot add `@Component`**.
- Instead, configure it as a bean:
  ```java
  @Configuration
  public class AwsConfig {
      @Bean
      public AmazonS3 s3Client() {
          return AmazonS3ClientBuilder.standard().build();
      }
  }
  ```

---


