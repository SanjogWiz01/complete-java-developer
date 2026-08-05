# Tomcat Development Checklist

Use this checklist before running a servlet project on Apache Tomcat.

## Environment

- JDK is installed and configured.
- Tomcat is downloaded and extracted.
- IDE has Tomcat server configured.
- Servlet API matches the Tomcat version.

## Project

- Web project structure is correct.
- Servlet class extends `HttpServlet`.
- URL mapping exists through annotation or `web.xml`.
- Application is deployed to Tomcat.
- Browser URL matches the servlet mapping.

## Debugging

- Check Tomcat console logs.
- Confirm package names and servlet class names.
- Confirm port number, usually `8080`.
- Restart Tomcat after configuration changes.
- Clean and redeploy if old classes are still being served.
