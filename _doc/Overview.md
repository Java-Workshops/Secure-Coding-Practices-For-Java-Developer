# Secure Coding Practices

## Supply Chain Security
### The Overview
### SCS For the Software Developer Process

## Fundamental Security Principals

## Thread Modelling - https://chatgpt.com/share/9b541993-5de6-4e5b-bdb4-3f632ce4db9c 
### Understanding threats and risks
### Common threat modeling frameworks (STRIDE, DREAD)
### Practical threat modeling exercise

## Groups For Security Testing Techniques
### SAST
### DAST
### IAST
### RASP

## Definitions
### SBOM
### Vulnerability
### Malicious Code Package
### CVSS - https://www.first.org/cvss/
#### CVSS 3.0/3.1
#### CVSS 4.0
### CWE - https://cwe.mitre.org/
### CVE - https://www.cve.org/
### EPSS - https://www.first.org/epss/

## Attack Vectors
### Dependency Confusion
#### Overview
#### Example with Java
### Typo Squatting
### Masquerading Attack
### Trojan Package
### Sensitive Data Stealer
#### TrivialDemo

## Obfuscating Techniques
### Obfuscators
### Bidirectional Characters 
https://svenruppert.com/2024/04/19/the-hidden-dangers-of-bidirectional-characters/
### Identifier Renaming:
    - Changing class, method, and variable names to meaningless or misleading names (e.g., renaming `calculateTotal` to `a`).
### Control Flow Obfuscation:
    - Altering the control flow to make the code less readable and harder to follow. This can involve adding bogus conditional statements, loops, or even goto-like jumps.
### String Encryption:
    - Encrypting strings in the code and decrypting them at runtime to prevent easy readability of hard-coded strings.
### Inline Code:
    - Replacing method calls with the actual method code inline, reducing the readability and making it harder to trace method calls.
### Dead Code Insertion:
    - Inserting code that has no effect on the program's functionality to confuse decompilers and human readers.
### Opaque Predicates:
    - Using expressions that are always true or false to make the control flow harder to understand.
### Dynamic Code Execution:
    - Generating code at runtime, often through reflection or dynamic proxies, to obscure the code's logic.
### Control Flow Flattening:
    - Transforming the control flow into a flat structure that relies on a dispatcher mechanism, making it harder to discern the original control flow.
### Class/Membership Obfuscation:
    - Splitting or merging classes and changing the membership of methods and variables between classes.
### Overloading and Polymorphism:
    - Using method overloading and polymorphism extensively to confuse the actual method being called.
### Exception Obfuscation:
    - Using try-catch blocks excessively and throwing exceptions that do not provide meaningful information.
### Anti-Tampering Checks:
    - Adding runtime checks to ensure the code has not been altered, and causing the program to malfunction if tampering is detected.
### Removing Debug Information:
    - Stripping out or obfuscating debug information that could aid in understanding the code, such as line numbers and source file names.
### Reflection and Dynamic Loading:
    - Using Java Reflection API to call methods and access fields dynamically, making static analysis and understanding more difficult.
### Annotations Obfuscation:
    - Obfuscating custom annotations and their processing logic to hide metadata and related behaviors.
### Aspect-Oriented Programming (AOP):
    - Using AOP techniques to weave additional behavior into the code at runtime, making the static code harder to analyze.

## Some CWE´s 
### CWE-488: Exposure of Data Element to Wrong Session
https://chatgpt.com/share/af664258-2d00-43de-bf6d-186465eab7cf
### CWE-022: Improper Limitation of a Pathname to a Restricted Directory
Blog - Secure Coding Practices - CWE-022 - Best practices to use Java NIO
### CWE-416: Use After Free
### CWE-787: Out-of-bounds Write
### CWE-1123: Excessive Use of Self-Modifying Code
### CWE-377: Insecure Temporary File incl TOCTOU
### und some more CWE´s

### CWE-20: Improper Input Validation
#### Basic Principles
#### SQL Injection
#### Core Java Implementation
Here we are working against a default PostgreSQL Stack 
with Core Java and JUnit5 Tests
#### Implementation with Apache Shiro
Implementing against an REST Endpoint, f.e. Javalin or Spark
#### Implementation with ESAPI

#### JakartaEE Security ??
#### Spring Security ??


### Remote Code Execution
[https://chatgpt.com/share/eebadccf-3b91-428e-a1de-8b1e53c07341](https://chatgpt.com/share/eebadccf-3b91-428e-a1de-8b1e53c07341)
#### Overview
#### Demo in Java/Rest ysoserial - https://github.com/frohoff/ysoserial
#### Demo Handwritten Exploit - 

## Dependency Management
### Principles
### Tools


## Test Techniques
### Mutation Testing
### Fuzzing / Monkey Testing


## Best Practices
### Monitoring / Logging
### Compile in Docker

## Harden a Webapp
Here we are hardening a webapp with some external Services.
We have to search for weak parts and change the implementation.



## Resources 
### Project SLSA - https://slsa.dev/
### OWASP - https://owasp.org/ 
### OWASP - Dependency Track - https://owasp.org/www-project-dependency-track/
