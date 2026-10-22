# Javadoc Style Guide for CyberGarage UPnP

This document defines the Javadoc documentation standards for the CyberGarage UPnP project.

## General Principles

1. **Clarity**: Documentation should be clear, concise, and actionable
2. **Completeness**: All public and protected APIs must be documented
3. **Consistency**: Follow the same structure and format throughout
4. **Accuracy**: Documentation must accurately reflect the code behavior

## Structure

### Class/Interface Documentation

```java
/**
 * One-sentence summary describing the class's primary responsibility.
 * 
 * <p>Additional context and details about the class behavior, usage patterns,
 * or important implementation notes. Use paragraph tags for multi-paragraph
 * descriptions.
 * 
 * <p>Thread-safety: Specify if the class is thread-safe or not.
 * 
 * <p>Example usage:
 * <pre>{@code
 * MyClass obj = new MyClass();
 * obj.doSomething();
 * }</pre>
 * 
 * @see RelatedClass
 * @since 2.1.5
 */
public class MyClass {
}
```

### Method Documentation

```java
/**
 * One-sentence summary of what the method does.
 * 
 * <p>Additional details about the method's behavior, side effects,
 * or important considerations.
 * 
 * @param paramName description of the parameter and its constraints
 * @param anotherParam description including valid values or ranges
 * @return description of the return value and what it represents
 * @throws ExceptionType when and why this exception is thrown
 * @see #relatedMethod()
 */
public ReturnType methodName(Type paramName, Type anotherParam) throws ExceptionType {
}
```

### Field Documentation

```java
/**
 * Brief description of what this field represents.
 */
public static final String CONSTANT_NAME = "value";
```

## Tag Order

Use tags in this order:
1. `@param` (one for each parameter, in declaration order)
2. `@return` (omit for void methods)
3. `@throws` or `@exception` (one for each exception type)
4. `@deprecated` (if applicable)
5. `@see` (references to related items)
6. `@since` (for newly added APIs)
7. `@author` (optional, use only for significant contributions - not required for this project)

## Formatting Guidelines

### Inline Tags

- Use `{@code ...}` for code snippets, variable names, and literals
- Use `{@link ClassName}` or `{@link ClassName#methodName}` for cross-references
- Use `{@value}` to display constant values

### HTML Tags

- Use `<p>` to separate paragraphs
- Use `<pre>{@code ...}</pre>` for multi-line code examples
- Use `<ul>` and `<li>` for bullet lists
- Avoid unnecessary HTML formatting

### Descriptions

- Start with a verb in third person (e.g., "Returns", "Creates", "Validates")
- Be specific about parameter constraints (null-allowed, ranges, valid values)
- Document thread-safety implications when relevant
- Explain performance characteristics for critical operations
- Document preconditions and postconditions

## Special Considerations

### UPnP-Specific Documentation

- Reference UPnP specifications when applicable
- Explain SSDP, SOAP, and GENA protocol details where relevant
- Document multicast and network behavior
- Note any platform-specific behavior

### Deprecated APIs

```java
/**
 * Brief description.
 * 
 * @deprecated Use {@link NewClass#newMethod()} instead. This method
 *             will be removed in version 3.0.
 */
@Deprecated
public void oldMethod() {
}
```

### Null Handling

Clearly document:
- Whether parameters can be null
- Whether return values can be null
- What happens if null is passed when not allowed

Example:
```java
/**
 * Processes the given device.
 * 
 * @param device the device to process, must not be {@code null}
 * @return the processed result, or {@code null} if processing failed
 * @throws NullPointerException if device is {@code null}
 */
```

## Examples

### Good Documentation Example

```java
/**
 * Represents a UPnP action that can be invoked on a service.
 * 
 * <p>Actions are methods exposed by UPnP services that can be called
 * remotely by control points. Each action has a name, a list of input
 * arguments, and a list of output arguments.
 * 
 * <p>This class is thread-safe. Multiple threads can safely invoke
 * the same action instance concurrently.
 * 
 * <p>Example usage:
 * <pre>{@code
 * Action action = service.getAction("SetVolume");
 * action.setArgumentValue("DesiredVolume", 50);
 * action.postControlAction();
 * }</pre>
 * 
 * @see Service
 * @see Argument
 * @since 2.1.5
 */
public class Action {
    /**
     * Posts this action to the device and waits for the response.
     * 
     * <p>This method sends a SOAP request to the device's control URL
     * and blocks until a response is received or a timeout occurs.
     * The response arguments can be retrieved after successful execution.
     * 
     * @return {@code true} if the action was executed successfully,
     *         {@code false} if an error occurred
     * @throws IllegalStateException if the action is not properly initialized
     * @see #getStatus()
     */
    public boolean postControlAction() throws IllegalStateException {
        // implementation
    }
}
```

## Verification

Before finalizing documentation:
1. Run `mvn javadoc:javadoc` to check for warnings
2. Review generated HTML for readability
3. Ensure all links resolve correctly
4. Verify code examples compile
